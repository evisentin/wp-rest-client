package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

import static io.github.evisentin.wordpress.rest.client.gutenberg.AdapterTestSupport.block;
import static io.github.evisentin.wordpress.rest.client.gutenberg.AdapterTestSupport.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("ParagraphBlockAdapter")
class ParagraphBlockAdapterTest {
    private final ParagraphBlockAdapter adapter = new ParagraphBlockAdapter();

    @Test
    @DisplayName("fromBlock extracts rich text and copies unknown attributes")
    void fromBlock__succeeds__when_savedParagraphHasUnknownOptions() {
        var source = block("<!-- wp:paragraph {\"unknown\":true} --><p class='custom'>A &amp; B</p><!-- /wp:paragraph -->");
        var model = adapter.fromBlock(source);
        assertThat(model.contentHtml()).isEqualTo("A &amp; B");
        assertThat(model.attributes()).containsEntry("unknown", true).isNotSameAs(source.attributes());
        assertThat(model.source()).isSameAs(source);
    }

    @ParameterizedTest
    @ValueSource(strings = {"left", "right"})
    @DisplayName("toBlock rejects locale-dependent aligned drop caps")
    void toBlock__fails__when_dropCapDependsOnLocale(String align) {
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpParagraphBlock("x", Map.of("dropCap", true,
                "style", Map.of("typography", Map.of("textAlign", align)))))).withMessageContaining("locale");
    }

    @ParameterizedTest
    @MethodSource("invalidOptions")
    @DisplayName("toBlock rejects unsupported or invalid new paragraph options")
    void toBlock__fails__when_newOptionsAreInvalid(Map<String, Object> attributes, String message) {
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpParagraphBlock("x", attributes)))
                                            .withMessage(message);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("nullText")
    @DisplayName("toBlock rejects null models and null rich text")
    void toBlock__fails__when_requiredModelDataIsNull(WpParagraphBlock model) {
        assertThatNullPointerException().isThrownBy(() -> adapter.toBlock(model));
    }

    @Test
    @DisplayName("toBlock rejects changes to existing comment options")
    void toBlock__fails__when_sourceOptionsChange() {
        var source = block("<!-- wp:paragraph --><p>x</p><!-- /wp:paragraph -->");
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpParagraphBlock("x", Map.of("className", "new"), source)))
                                            .withMessageContaining("Changing saved block options");
    }

    @Test
    @DisplayName("toBlock suppresses drop caps on centered paragraphs")
    void toBlock__succeeds__when_dropCapIsCentered() {
        var result = adapter.toBlock(new WpParagraphBlock("Hello", Map.of("dropCap", true,
                "style", Map.of("typography", Map.of("textAlign", "center")))));
        assertThat(html(result).selectFirst("p").classNames()).containsExactly("has-text-align-center");
    }

    @Test
    @DisplayName("toBlock omits drop-cap markup when the option is false")
    void toBlock__succeeds__when_dropCapIsDisabled() {
        var result = adapter.toBlock(new WpParagraphBlock("Hello", Map.of("dropCap", false)));
        assertThat(html(result).selectFirst("p").outerHtml()).isEqualTo("<p>Hello</p>");
    }

    @Test
    @DisplayName("toBlock edits rich text while preserving existing wrapper attributes")
    void toBlock__succeeds__when_existingTextChanges() {
        var source = block("<!-- wp:paragraph {\"unknown\":true} --><p class='custom' data-test='x'>Old</p><!-- /wp:paragraph -->");
        var model = adapter.fromBlock(source);
        var result = adapter.toBlock(new WpParagraphBlock("<strong>New</strong>", model.attributes(), source));
        var paragraph = html(result).selectFirst("p");
        assertThat(paragraph.html()).isEqualTo("<strong>New</strong>");
        assertThat(paragraph.classNames()).containsExactly("custom");
        assertThat(paragraph.attr("data-test")).isEqualTo("x");
        assertThat(result.attributes()).containsEntry("unknown", true);
        assertThat(html(source).selectFirst("p").text()).isEqualTo("Old");
    }

    @Test
    @DisplayName("toBlock returns the original source when no fields change")
    void toBlock__succeeds__when_modelIsUnchanged() {
        var source = block("<!-- wp:paragraph --><p class='custom'>A &amp; B</p><!-- /wp:paragraph -->");
        assertThat(adapter.toBlock(adapter.fromBlock(source))).isSameAs(source);
    }

    @Test
    @DisplayName("toBlock creates a plain paragraph without optional attributes")
    void toBlock__succeeds__when_newParagraphHasNoOptions() {
        var result = adapter.toBlock(new WpParagraphBlock("Hello", Map.of()));
        assertThat(result.attributes()).isEmpty();
        assertThat(html(result).selectFirst("p").outerHtml()).isEqualTo("<p>Hello</p>");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ltr", "rtl"})
    @DisplayName("toBlock creates paragraphs with classes, anchor, direction and a drop cap")
    void toBlock__succeeds__when_newParagraphHasSupportedOptions(String direction) {
        var result = adapter.toBlock(new WpParagraphBlock("Hello", Map.of("className", "first second",
                "anchor", "intro", "direction", direction, "dropCap", true)));
        var paragraph = html(result).selectFirst("p");
        assertThat(paragraph.classNames()).containsExactlyInAnyOrder("first", "second", "has-drop-cap");
        assertThat(paragraph.id()).isEqualTo("intro");
        assertThat(paragraph.attr("dir")).isEqualTo(direction);
        assertThat(paragraph.text()).isEqualTo("Hello");
    }

    static Stream<Arguments> invalidOptions() {
        return Stream.of(Arguments.of(Map.of("direction", "auto"), "Invalid paragraph direction"),
                Arguments.of(Map.of("dropCap", "yes"), "dropCap must be a boolean"),
                Arguments.of(Map.of("unknown", true), "Unsupported option for new block: unknown"));
    }

    static Stream<WpParagraphBlock> nullText() {return Stream.of(new WpParagraphBlock(null, Map.of()));}
}
