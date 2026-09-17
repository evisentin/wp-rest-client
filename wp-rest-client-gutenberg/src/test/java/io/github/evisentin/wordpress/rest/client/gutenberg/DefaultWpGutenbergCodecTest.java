package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultWpGutenbergCodecTest {
    private final DefaultWpGutenbergCodec codec = new DefaultWpGutenbergCodec();

    @Test
    void distinguishesEmptyPairedAndSelfClosingBlocks() {
        String raw = "<!-- wp:group --><!-- /wp:group --><!-- wp:latest-posts /-->";
        assertThat(codec.serialize(codec.parse(raw))).isEqualTo(raw);
    }

    @Test
    void escapesAttributesWithoutCorruptingJsonStrings() {
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("value", "--><&\"\\\n日本語");
        attributes.put("literalEscape", "\\\"\\u0022");
        attributes.put("missing", null);
        WpBlock block = new WpBlock("vendor/widget", attributes, List.of(), WpBlockSyntax.SELF_CLOSING);
        WpBlockDocument document = new WpBlockDocument(List.of(block));
        String serialized = codec.serialize(document);
        assertThat(serialized).contains("\\u002d\\u002d\\u003e\\u003c\\u0026\\u0022\\u005c");
        assertThat(codec.parse(serialized)).isEqualTo(document);
    }

    @Test
    void handlesDeeplyNestedContentWithoutRecursiveTraversal() {
        String raw = "<!-- wp:group -->".repeat(2000) + "x" + "<!-- /wp:group -->".repeat(2000);
        assertThat(codec.serialize(codec.parse(raw))).isEqualTo(raw);
    }

    @Test
    void handlesWhitespaceAroundCommentBodiesWithoutChangingLiteralComments() {
        String raw = "<!-- ordinary wp:paragraph -->"
                     + "<!--\u2003wp:paragraph\u2003--><p>x</p><!--\t/wp:paragraph\n-->";
        assertThat(codec.serialize(codec.parse(raw))).isEqualTo("<!-- ordinary wp:paragraph -->"
                                                                + "<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->");
    }

    @Test
    void normalizesCoreNamesAndDelimiterSpacingOnly() {
        assertThat(codec.serialize(codec.parse("<!--  wp:core/paragraph   --><p>x</p><!-- /wp:core/paragraph-->")))
                .isEqualTo("<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->");
    }

    @Test
    void preservesClassicContentAndOrdinaryComments() {
        for (String raw : List.of("", " \n", "<p>Classic</p><!-- ordinary -->", "<!-- incomplete")) {
            assertThat(codec.serialize(codec.parse(raw))).isEqualTo(raw);
        }
    }

    @Test
    void preservesNestedBlocksAndSurroundingHtml() {
        String raw = "before\n<!-- wp:group {\"layout\":{\"type\":\"constrained\"}} -->"
                     + "<div>\n<!-- wp:paragraph --><p>Hello <strong>world</strong></p><!-- /wp:paragraph -->"
                     + "\n<!-- wp:vendor/widget {\"items\":[1,true,null]} /--></div><!-- /wp:group -->\nafter";
        WpBlockDocument document = codec.parse(raw);
        WpBlock group = (WpBlock) document.nodes().get(1);
        assertThat(group.name()).isEqualTo("core/group");
        assertThat(group.content()).hasSize(5);
        assertThat(((WpBlock) group.content().get(3)).name()).isEqualTo("vendor/widget");
        assertThat(codec.serialize(document)).isEqualTo(raw);
        assertThat(codec.parse(codec.serialize(document))).isEqualTo(document);
    }

    @Test
    void preservesWideSequentialListsInOrder() {
        List<WpContentNode> nodes = new LinkedList<>();
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 2000; i++) {
            String html = "<p>" + i + "</p>";
            nodes.add(new WpHtmlFragment(html));
            expected.append(html);
        }
        assertThat(codec.serialize(new WpBlockDocument(nodes))).isEqualTo(expected.toString());
    }

    @Test
    void rejectsInvalidModelsRatherThanDroppingContent() {
        assertThatThrownBy(() -> codec.serialize(new WpBlockDocument(List.of(
                new WpBlock("core/image", Map.of(), List.of(new WpHtmlFragment("x")), WpBlockSyntax.SELF_CLOSING)))))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> codec.serialize(new WpBlockDocument(List.of(
                new WpBlock("bad -->", Map.of(), List.of(), WpBlockSyntax.PAIRED)))))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> codec.parse(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsMalformedBlocksWithOffsets() {
        for (String raw : List.of("<!-- /wp:paragraph -->", "<!-- wp:paragraph -->",
                "<!-- wp:paragraph --><!-- /wp:group -->", "<!-- wp:paragraph {bad} -->",
                "<!-- wp:paragraph [] /-->", "<!-- wp:paragraph {} trailing /-->",
                "<!-- wp:paragraph", "<!-- wp:bad/name/extra /-->")) {
            assertThatThrownBy(() -> codec.parse(raw)).as(raw).isInstanceOf(WpBlockParseException.class);
        }
        assertThatExceptionOfType(WpBlockParseException.class)
                .isThrownBy(() -> codec.parse("abc<!-- wp:paragraph -->"))
                .satisfies(error -> assertThat(error.getOffset()).isEqualTo(3));
    }
}
