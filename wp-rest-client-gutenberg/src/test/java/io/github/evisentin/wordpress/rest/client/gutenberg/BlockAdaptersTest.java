package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.GroupBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.HeadingBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.ImageBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.ParagraphBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlockAdaptersTest {
    private final DefaultWpGutenbergCodec codec = new DefaultWpGutenbergCodec();

    @Test
    void createsBasicBlocksWithEscapedHtmlAttributes() {
        var paragraph = new ParagraphBlockAdapter().toBlock(new WpParagraphBlock("Hello", Map.of("style", Map.of("typography", Map.of("textAlign", "center")))));
        assertThat(serialize(paragraph)).contains("<p class=\"has-text-align-center\">Hello</p>");
        var heading = new HeadingBlockAdapter().toBlock(new WpHeadingBlock("Title", 4, Map.of()));
        assertThat(heading.attributes()).containsEntry("level", 4);
        var image = new ImageBlockAdapter().toBlock(new WpImageBlock(2L, "image.jpg", "a&b", null, Map.of()));
        assertThat(serialize(image)).contains("alt=\"a&amp;b\"", "wp-image-2");
    }

    @Test
    void groupRetainsNestedBlockPositionsAndUnknownAttributes() {
        var adapter = new GroupBlockAdapter();
        var source = block("<!-- wp:group {\"layout\":{\"type\":\"flex\"}} --><div>"
                           + "<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph --></div><!-- /wp:group -->");
        assertThat(adapter.toBlock(adapter.fromBlock(source))).isEqualTo(source);
    }

    @Test
    void headingUpdatesLevelInHtmlAndCommentAndUsesDefaultLevelTwo() {
        var adapter = new HeadingBlockAdapter();
        var model = adapter.fromBlock(block("<!-- wp:heading {\"level\":3} --><h3 class=\"wp-block-heading\">Title</h3><!-- /wp:heading -->"));
        assertThat(model.level()).isEqualTo(3);
        var updated = adapter.toBlock(new WpHeadingBlock("New", 2, model.attributes(), model.source()));
        assertThat(updated.attributes()).doesNotContainKey("level");
        assertThat(serialize(updated)).contains("<h2 class=\"wp-block-heading\">New</h2>");
        assertThatThrownBy(() -> adapter.toBlock(new WpHeadingBlock("x", 7, Map.of())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void imagePreservesLinkAndUpdatesMediaIdUrlAndCaption() {
        var adapter = new ImageBlockAdapter();
        WpBlock source = block("<!-- wp:image {\"id\":42,\"linkDestination\":\"media\"} -->"
                               + "<figure class=\"wp-block-image\"><a href=\"old.jpg\"><img src=\"old.jpg\" alt=\"Old\" "
                               + "class=\"custom wp-image-42\" srcset=\"old.jpg 1x\"/></a><figcaption>Old caption</figcaption></figure><!-- /wp:image -->");
        var model = adapter.fromBlock(source);
        assertThat(adapter.toBlock(model)).isSameAs(source);
        var updated = adapter.toBlock(new WpImageBlock(43L, "new.jpg?a=1&b=2", "New \"alt\"", "<em>Caption</em>",
                model.attributes(), model.source()));
        assertThat(updated.attributes()).containsEntry("id", 43L);
        assertThat(serialize(updated)).contains("wp-image-43", "custom", "<em>Caption</em>", "href=\"new.jpg?a=1&amp;b=2\"")
                                      .doesNotContain("srcset", "wp-image-42");
        assertThat(adapter.fromBlock(updated).altText()).isEqualTo("New \"alt\"");
    }

    @Test
    void paragraphPreservesOriginalAndEditsRichTextInsideExistingWrapper() {
        var adapter = new ParagraphBlockAdapter();
        WpBlock source = block("<!-- wp:paragraph {\"unknown\":true} --><p class='custom' data-test='x'>A &amp; B</p><!-- /wp:paragraph -->");
        var model = adapter.fromBlock(source);
        assertThat(adapter.toBlock(model)).isSameAs(source);
        var updated = adapter.toBlock(new WpParagraphBlock("<strong>New</strong>", model.attributes(), model.source()));
        assertThat(serialize(updated)).contains("<strong>New</strong>", "class=\"custom\"", "data-test=\"x\"");
        assertThat(updated.attributes()).containsEntry("unknown", true);
        assertThatThrownBy(() -> adapter.toBlock(new WpParagraphBlock("New", Map.of("style", Map.of()))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsWrongBlockNamesAndUnsupportedLeafStructures() {
        var adapter = new ParagraphBlockAdapter();
        assertThatThrownBy(() -> adapter.fromBlock(block("<!-- wp:heading --><h2>x</h2><!-- /wp:heading -->")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> adapter.fromBlock(block("<!-- wp:paragraph --><div>x</div><!-- /wp:paragraph -->")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private WpBlock block(String raw) {
        return (WpBlock) codec.parse(raw).nodes().getFirst();
    }

    private String serialize(WpBlock block) {
        return codec.serialize(new WpBlockDocument(List.of(block)));
    }
}
