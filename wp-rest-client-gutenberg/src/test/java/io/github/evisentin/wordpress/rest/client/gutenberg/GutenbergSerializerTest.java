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
}
