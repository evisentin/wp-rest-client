package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.HeadingBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.ImageBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.ParagraphBlockAdapter;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdapterEditingTest {
    private final DefaultWpGutenbergCodec codec = new DefaultWpGutenbergCodec();
    private final ParagraphBlockAdapter paragraphs = new ParagraphBlockAdapter();
    private final HeadingBlockAdapter headings = new HeadingBlockAdapter();
    private final ImageBlockAdapter images = new ImageBlockAdapter();

    @Test
    void addingCaptionToBareImageFailsInsteadOfLosingMarkup() {
        var original = images.fromBlock(parse("<!-- wp:image --><img src=\"x.jpg\"><!-- /wp:image -->"));
        assertThatThrownBy(() -> images.toBlock(new WpImageBlock(null, "x.jpg", "", "Caption", original.attributes(), original.source())))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("figure");
    }

    @Test
    void addsCaptionWithoutChangingResponsiveSourcesWhenUrlIsUnchanged() {
        var original = images.fromBlock(parse("<!-- wp:image --><figure><img src=\"x.jpg\" srcset=\"x.jpg 1x\" "
                                              + "sizes=\"100vw\" alt=\"\"></figure><!-- /wp:image -->"));
        var updated = images.toBlock(new WpImageBlock(null, "x.jpg", "alt", "<em>Caption</em>", original.attributes(), original.source()));
        var document = html(updated);
        assertThat(document.selectFirst("figcaption").html()).isEqualTo("<em>Caption</em>");
        assertThat(document.selectFirst("figcaption").classNames()).contains("wp-element-caption");
        assertThat(document.selectFirst("img").attr("srcset")).isEqualTo("x.jpg 1x");
        assertThat(document.selectFirst("img").attr("sizes")).isEqualTo("100vw");
    }

    @Test
    void centeredParagraphSuppressesDropCap() {
        var block = paragraphs.toBlock(new WpParagraphBlock("Hello", Map.of("dropCap", true,
                "style", Map.of("typography", Map.of("textAlign", "center")))));
        assertThat(html(block).selectFirst("p").classNames()).contains("has-text-align-center").doesNotContain("has-drop-cap");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void createsAllHeadingLevelsAndRecoversRichText(int level) {
        var block = headings.toBlock(new WpHeadingBlock("Title <em>&amp; subtitle</em>", level, Map.of()));
        assertThat(html(block).selectFirst("h" + level).html()).isEqualTo("Title <em>&amp; subtitle</em>");
        assertThat(headings.fromBlock(block).level()).isEqualTo(level);
        if (level == 2) {
            assertThat(block.attributes()).doesNotContainKey("level");
        } else {
            assertThat(block.attributes()).containsEntry("level", level);
        }
    }

    @Test
    void createsParagraphWithClassesAnchorDirectionAndDropCap() {
        var block = paragraphs.toBlock(new WpParagraphBlock("Hello", Map.of(
                "className", "first second", "anchor", "intro", "direction", "rtl", "dropCap", true)));
        var p = html(block).selectFirst("p");
        assertThat(p.classNames()).containsExactlyInAnyOrder("first", "second", "has-drop-cap");
        assertThat(p.id()).isEqualTo("intro");
        assertThat(p.attr("dir")).isEqualTo("rtl");
    }

    @Test
    void refusesChangedSourceOptionsForEverySemanticAdapter() {
        var p = paragraphs.fromBlock(parse("<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->"));
        assertThatThrownBy(() -> paragraphs.toBlock(new WpParagraphBlock("x", Map.of("className", "changed"), p.source())))
                .isInstanceOf(IllegalArgumentException.class);
        var h = headings.fromBlock(parse("<!-- wp:heading --><h2>x</h2><!-- /wp:heading -->"));
        assertThatThrownBy(() -> headings.toBlock(new WpHeadingBlock("x", 2, Map.of("className", "changed"), h.source())))
                .isInstanceOf(IllegalArgumentException.class);
        var i = images.fromBlock(parse("<!-- wp:image --><figure><img src=\"x.jpg\" alt=\"\"></figure><!-- /wp:image -->"));
        assertThatThrownBy(() -> images.toBlock(new WpImageBlock(null, "x.jpg", "", null, Map.of("sizeSlug", "large"), i.source())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"left", "right"})
    void refusesLocaleDependentAlignedDropCaps(String align) {
        assertThatThrownBy(() -> paragraphs.toBlock(new WpParagraphBlock("x", Map.of("dropCap", true,
                "style", Map.of("typography", Map.of("textAlign", align))))))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("locale");
    }

    @ParameterizedTest
    @ValueSource(strings = {"3", "2.5", "\"2\""})
    void rejectsHeadingLevelInconsistentWithMarkup(String level) {
        var block = parse("<!-- wp:heading {\"level\":" + level + "} --><h2>Title</h2><!-- /wp:heading -->");
        assertThatThrownBy(() -> headings.fromBlock(block)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "1.5", "\"42\""})
    void rejectsInvalidImageIds(String id) {
        assertThatThrownBy(() -> images.fromBlock(parse("<!-- wp:image {\"id\":" + id + "} -->"
                                                        + "<figure><img src=\"x.jpg\"></figure><!-- /wp:image -->")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<figure></figure>", "<figure><img src='a'><img src='b'></figure>"})
    void rejectsMissingOrAmbiguousImages(String markup) {
        assertThatThrownBy(() -> images.fromBlock(parse("<!-- wp:image -->" + markup + "<!-- /wp:image -->")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removesCaptionAndMediaIdWithoutRemovingUnrelatedClassesOrCustomLink() {
        var original = images.fromBlock(parse("<!-- wp:image {\"id\":42,\"linkDestination\":\"custom\"} -->"
                                              + "<figure><a href=\"https://example.com/page\"><img src=\"old.jpg\" class=\"custom wp-image-42\" "
                                              + "srcset=\"old.jpg 1x\" sizes=\"100vw\" alt=\"old\"></a><figcaption>Caption</figcaption></figure><!-- /wp:image -->"));
        var updated = images.toBlock(new WpImageBlock(null, "new.jpg", null, null, original.attributes(), original.source()));
        var document = html(updated);
        assertThat(updated.attributes()).doesNotContainKey("id");
        assertThat(document.select("figcaption")).isEmpty();
        assertThat(document.selectFirst("img").classNames()).containsExactly("custom");
        assertThat(document.selectFirst("img").hasAttr("srcset")).isFalse();
        assertThat(document.selectFirst("img").hasAttr("sizes")).isFalse();
        assertThat(document.selectFirst("img").attr("alt")).isEmpty();
        assertThat(document.selectFirst("a").attr("href")).isEqualTo("https://example.com/page");
    }

    private org.jsoup.nodes.Document html(WpBlock block) {
        return Jsoup.parseBodyFragment(codec.serialize(new WpBlockDocument(List.of(block))));
    }

    private WpBlock parse(String markup) {
        return (WpBlock) codec.parse(markup).nodes().getFirst();
    }
}
