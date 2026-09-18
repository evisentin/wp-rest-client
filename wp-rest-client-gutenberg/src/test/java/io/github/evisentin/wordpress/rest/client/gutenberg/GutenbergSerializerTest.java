package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("GutenbergSerializer")
class GutenbergSerializerTest {
    private final GutenbergSerializer serializer = new GutenbergSerializer();

    @Test
    @DisplayName("serialize reports JSON serialization failures with the original cause")
    void serialize__fails__when_attributesCannotBeSerialized() {
        var block = new WpBlock("core/paragraph", Map.of("unsupported", new Object()), List.of(), WpBlockSyntax.PAIRED);
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(new WpBlockDocument(List.of(block))))
                                            .withMessage("Block attributes cannot be serialized as JSON")
                                            .withCauseInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidBlocks")
    @DisplayName("serialize rejects invalid block structures without silently dropping content")
    void serialize__fails__when_blockIsInvalid(WpBlock block, String message) {
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(new WpBlockDocument(List.of(block))))
                                            .withMessage(message);
    }

    @ParameterizedTest
    @MethodSource("nullDocuments")
    @DisplayName("serialize rejects null documents, node lists and nodes")
    void serialize__fails__when_documentContainsNulls(WpBlockDocument document) {
        assertThatNullPointerException().isThrownBy(() -> serializer.serialize(document));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("escapedValues")
    @DisplayName("serialize emits exact comment-safe JSON for escaped characters and adjacent escape sequences")
    void serialize__succeeds__when_attributeContainsCommentSensitiveText(String value, String escaped) {
        var document = new WpBlockDocument(List.of(
                new WpBlock("vendor/widget", Map.of("value", value), List.of(), WpBlockSyntax.SELF_CLOSING)));
        String result = serializer.serialize(document);
        assertThat(result).isEqualTo("<!-- wp:vendor/widget {\"value\":\"" + escaped + "\"} /-->");
        assertThat(new GutenbergParser().parse(result)).isEqualTo(document);
    }

    @Test
    @DisplayName("serialize escapes comment-sensitive characters without corrupting JSON strings or null values")
    void serialize__succeeds__when_attributesContainEscapesAndUnicode() {
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("value", "--><&\"\\\n日本語-single-");
        attributes.put("literalEscape", "\\\"\\u0022");
        attributes.put("missing", null);
        var document = new WpBlockDocument(List.of(new WpBlock("vendor/widget", attributes, List.of(), WpBlockSyntax.SELF_CLOSING)));
        String result = serializer.serialize(document);
        assertThat(result).contains("\\u002d\\u002d\\u003e\\u003c\\u0026\\u0022\\u005c", "\\n日本語-single-", "\"missing\":null");
        assertThat(new GutenbergParser().parse(result)).isEqualTo(document);
    }

    @Test
    @DisplayName("serialize visits wide linked lists in order")
    void serialize__succeeds__when_contentUsesSequentialLists() {
        List<WpContentNode> nodes = new LinkedList<>();
        StringBuilder expected = new StringBuilder();
        for (int index = 0; index < 2000; index++) {
            String html = "<p>" + index + "</p>";
            nodes.add(new WpHtmlFragment(html));
            expected.append(html);
        }
        assertThat(serializer.serialize(new WpBlockDocument(nodes))).isEqualTo(expected.toString());
    }

    @Test
    @DisplayName("serialize retains literal HTML and emits paired, self-closing and plugin delimiters")
    void serialize__succeeds__when_documentContainsMixedNodes() {
        var child = new WpBlock("vendor/widget", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING);
        var group = new WpBlock("core/group", Map.of(),
                List.of(new WpHtmlFragment("<div>"), child, new WpHtmlFragment("</div>")), WpBlockSyntax.PAIRED);
        var document = new WpBlockDocument(List.of(new WpHtmlFragment("before"), group, new WpHtmlFragment("after")));
        assertThat(serializer.serialize(document)).isEqualTo(
                "before<!-- wp:group --><div><!-- wp:vendor/widget /--></div><!-- /wp:group -->after");
    }

    @Test
    @DisplayName("serialize returns an empty string for an empty document")
    void serialize__succeeds__when_documentIsEmpty() {
        assertThat(serializer.serialize(new WpBlockDocument(List.of()))).isEmpty();
    }

    @Test
    @DisplayName("serialize keeps adjacent HTML fragments together and ignores empty fragments in pretty mode")
    void serialize__succeeds__when_prettyContentContainsAdjacentFragments() {
        var block = new WpBlock("core/paragraph", Map.of(), List.of(new WpHtmlFragment(""),
                new WpHtmlFragment("<p>Hello "), new WpHtmlFragment("<em>world</em></p>")), WpBlockSyntax.PAIRED);
        assertThat(new GutenbergSerializer().serialize(new WpBlockDocument(List.of(block)), true))
                .isEqualTo("<!-- wp:paragraph -->\n<p>Hello <em>world</em></p>\n<!-- /wp:paragraph -->");
    }

    @Test
    @DisplayName("serialize pretty prints deeply nested blocks without recursive traversal")
    void serialize__succeeds__when_prettyContentIsDeeplyNested() {
        String raw = "<!-- wp:group -->".repeat(2000) + "x" + "<!-- /wp:group -->".repeat(2000);
        var document = new GutenbergParser().parse(raw);
        assertThat(new GutenbergSerializer().serialize(document, true))
                .isEqualTo("<!-- wp:group -->\n".repeat(2000) + "x" + "\n<!-- /wp:group -->".repeat(2000));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("prettyDocuments")
    @DisplayName("serialize formats block boundaries without rewriting saved HTML or adding trailing newlines")
    void serialize__succeeds__when_prettyPrintIsEnabled(String raw, String expected) {
        var parser = new GutenbergParser();
        var pretty = new GutenbergSerializer();
        var document = parser.parse(raw);
        assertThat(pretty.serialize(document, true)).isEqualTo(expected);
        assertThat(pretty.serialize(parser.parse(expected), true)).isEqualTo(expected);
        assertThat(pretty.serialize(document, false)).isEqualTo(raw);
    }

    static Stream<Arguments> escapedValues() {
        return Stream.of(
                Arguments.of("--", "\\u002d\\u002d"),
                Arguments.of("---", "\\u002d\\u002d-"),
                Arguments.of("<>&", "\\u003c\\u003e\\u0026"),
                Arguments.of("\"", "\\u0022"),
                Arguments.of("\\", "\\u005c"),
                Arguments.of("\\\"", "\\u005c\\u0022"),
                Arguments.of("\\\\\"", "\\u005c\\u005c\\u0022"),
                Arguments.of("\\u0022", "\\u005cu0022"),
                Arguments.of("\n\r\t\b\f", "\\n\\r\\t\\b\\f"),
                Arguments.of("日本語😀", "日本語😀"));
    }

    static Stream<Arguments> invalidBlocks() {
        return Stream.of(
                Arguments.of(new WpBlock(null, Map.of(), List.of(), WpBlockSyntax.PAIRED), "Block name must be namespace/name: null"),
                Arguments.of(new WpBlock("bad -->", Map.of(), List.of(), WpBlockSyntax.PAIRED), "Block name must be namespace/name: bad -->"),
                Arguments.of(new WpBlock("core/image", null, List.of(), WpBlockSyntax.PAIRED), "Block attributes cannot be null"),
                Arguments.of(new WpBlock("core/image", Map.of(), null, WpBlockSyntax.PAIRED), "Block content cannot be null"),
                Arguments.of(new WpBlock("core/image", Map.of(), List.of(), null), "Block syntax cannot be null"),
                Arguments.of(new WpBlock("core/image", Map.of(), List.of(new WpHtmlFragment("x")), WpBlockSyntax.SELF_CLOSING),
                        "Self-closing block cannot contain content"));
    }

    static Stream<Arguments> nullDocuments() {
        return Stream.of(Arguments.of((Object) null), Arguments.of(new WpBlockDocument(null)),
                Arguments.of(new WpBlockDocument(Arrays.asList((WpContentNode) null))));
    }

    static Stream<Arguments> prettyDocuments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("<p>Classic <em>HTML</em></p>", "<p>Classic <em>HTML</em></p>"),
                Arguments.of("<!-- wp:paragraph --><p>Hello <strong>world</strong>!</p><!-- /wp:paragraph -->",
                        "<!-- wp:paragraph -->\n<p>Hello <strong>world</strong>!</p>\n<!-- /wp:paragraph -->"),
                Arguments.of("<!-- wp:group --><!-- /wp:group -->", "<!-- wp:group -->\n<!-- /wp:group -->"),
                Arguments.of("<!-- wp:latest-posts /--><!-- wp:latest-comments /-->",
                        "<!-- wp:latest-posts /-->\n<!-- wp:latest-comments /-->"),
                Arguments.of("<!-- wp:group --><div><!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->"
                             + "<!-- wp:vendor/widget {\"items\":[1,2]} /--></div><!-- /wp:group -->",
                        "<!-- wp:group -->\n<div>\n<!-- wp:paragraph -->\n<p>x</p>\n<!-- /wp:paragraph -->\n"
                        + "<!-- wp:vendor/widget {\"items\":[1,2]} /-->\n</div>\n<!-- /wp:group -->"),
                Arguments.of("before<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->after",
                        "before\n<!-- wp:paragraph -->\n<p>x</p>\n<!-- /wp:paragraph -->\nafter"),
                Arguments.of("<!-- wp:paragraph -->\n<p>x</p>\n<!-- /wp:paragraph -->\n",
                        "<!-- wp:paragraph -->\n<p>x</p>\n<!-- /wp:paragraph -->\n"),
                Arguments.of("<!-- wp:paragraph -->\r\n<p>x</p>\r\n<!-- /wp:paragraph -->\r\n",
                        "<!-- wp:paragraph -->\r\n<p>x</p>\r\n<!-- /wp:paragraph -->\r\n"),
                Arguments.of("<!-- wp:paragraph -->\r<p>x</p>\r<!-- /wp:paragraph -->",
                        "<!-- wp:paragraph -->\r<p>x</p>\r<!-- /wp:paragraph -->"),
                Arguments.of("<!-- wp:preformatted --><pre>  a\n    b\n</pre><!-- /wp:preformatted -->",
                        "<!-- wp:preformatted -->\n<pre>  a\n    b\n</pre>\n<!-- /wp:preformatted -->"));
    }
}
