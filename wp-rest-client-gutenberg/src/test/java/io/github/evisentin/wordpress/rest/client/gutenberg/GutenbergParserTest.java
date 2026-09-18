package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("GutenbergParser")
class GutenbergParserTest {
    private final GutenbergParser parser = new GutenbergParser();

    @ParameterizedTest(name = "{0}")
    @MethodSource("malformedContent")
    @DisplayName("parse rejects malformed syntax with the exact delimiter offset and cause")
    void parse__fails__when_blockSyntaxIsMalformed(String raw, int offset, String message, boolean jsonCause) {
        assertThatExceptionOfType(WpBlockParseException.class).isThrownBy(() -> parser.parse(raw))
                                                              .withMessage(message).satisfies(error -> {
                                                                  assertThat(error.getOffset()).isEqualTo(offset);
                                                                  if (jsonCause) {
                                                                      assertThat(error.getCause()).isInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
                                                                  } else {
                                                                      assertThat(error.getCause()).isNull();
                                                                  }
                                                              });
    }

    @Test
    @DisplayName("parse rejects null raw content")
    void parse__fails__when_contentIsNull() {
        assertThatNullPointerException().isThrownBy(() -> parser.parse(null));
    }

    @Test
    @DisplayName("parse distinguishes empty paired and self-closing blocks")
    void parse__succeeds__when_blocksHaveDifferentDelimiterForms() {
        assertThat(parser.parse("<!--wp:group--><!--/wp:group--><!--wp:latest-posts /-->").nodes())
                .containsExactly(new WpBlock("core/group", Map.of(), List.of(), WpBlockSyntax.PAIRED),
                        new WpBlock("core/latest-posts", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING));
    }

    @Test
    @DisplayName("parse preserves nested blocks, wrapper positions, unknown attributes and outside HTML")
    void parse__succeeds__when_contentContainsNestedPluginBlocks() {
        var child = new WpBlock("vendor/widget", Map.of("items", java.util.Arrays.asList(1, true, null)),
                List.of(), WpBlockSyntax.SELF_CLOSING);
        var group = new WpBlock("core/group", Map.of("layout", Map.of("type", "flex")),
                List.of(new WpHtmlFragment("<div>"), child, new WpHtmlFragment("</div>")), WpBlockSyntax.PAIRED);
        var document = parser.parse("before<!-- wp:group {\"layout\":{\"type\":\"flex\"}} --><div>"
                                    + "<!-- wp:vendor/widget {\"items\":[1,true,null]} /--></div><!-- /wp:group -->after");
        assertThat(document.nodes()).containsExactly(new WpHtmlFragment("before"), group, new WpHtmlFragment("after"));
        assertThatThrownBy(() -> document.nodes().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {" \n", "<p>Classic</p><!-- ordinary -->", "<!-- incomplete", "<!--", "<!--   ",
            "<!---->", "<!--   -->", "<!-- ordinary wp:paragraph -->", "<!--more--><!--nextpage-->"})
    @DisplayName("parse preserves classic HTML and ordinary comments as one literal fragment")
    void parse__succeeds__when_contentHasNoBlockDelimiters(String raw) {
        assertThat(parser.parse(raw).nodes()).containsExactly(new WpHtmlFragment(raw));
    }

    @Test
    @DisplayName("parse returns an empty document for empty content")
    void parse__succeeds__when_contentIsEmpty() {
        assertThat(parser.parse("").nodes()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"paragraph", "core/paragraph"})
    @DisplayName("parse normalizes core names and accepts delimiter whitespace")
    void parse__succeeds__when_coreNameIsShortOrQualified(String name) {
        var document = parser.parse("<!--\u2003wp:" + name + "\u2003--><p>x</p><!--\t/wp:" + name + "\n-->");
        assertThat(document.nodes()).containsExactly(new WpBlock("core/paragraph", Map.of(),
                List.of(new WpHtmlFragment("<p>x</p>")), WpBlockSyntax.PAIRED));
    }

    static Stream<Arguments> malformedContent() {
        return Stream.of(
                Arguments.of("<!-- /wp:paragraph -->", 0, "Unexpected closing block: core/paragraph", false),
                Arguments.of("<!-- wp:paragraph --><!-- /wp:group -->", 21, "Unexpected closing block: core/group", false),
                Arguments.of("<!-- wp:paragraph --><!-- /wp:paragraph {} -->", 21, "Unexpected closing block: core/paragraph", false),
                Arguments.of("abc<!-- wp:paragraph -->", 3, "Unclosed block: core/paragraph", false),
                Arguments.of("😀<!-- wp:paragraph", 2, "Unterminated block comment", false),
                Arguments.of("<!-- wp:bad/name/extra /-->", 0, "Invalid block delimiter", false),
                Arguments.of("<!-- wp:paragraph [] /-->", 0, "Block attributes must be a JSON object", false),
                Arguments.of("<!-- wp:paragraph {bad} -->", 0, "Invalid block attributes", true),
                Arguments.of("<!-- wp:paragraph {} trailing /-->", 0, "Invalid block attributes", true));
    }
}
