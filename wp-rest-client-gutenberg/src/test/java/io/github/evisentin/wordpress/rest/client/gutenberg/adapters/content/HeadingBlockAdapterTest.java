package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpHeadingBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

@DisplayName("HeadingBlockAdapter")
class HeadingBlockAdapterTest {
    private final HeadingBlockAdapter adapter = new HeadingBlockAdapter();

    @ParameterizedTest
    @ValueSource(strings = {"3", "2.5", "\"2\""})
    @DisplayName("fromBlock rejects declared levels that disagree with saved HTML")
    void fromBlock__fails__when_declaredLevelDoesNotMatchMarkup(String level) {
        var source = block("<!-- wp:heading {\"level\":" + level + "} --><h2>Title</h2><!-- /wp:heading -->");
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.fromBlock(source)).withMessage("Heading level does not match saved HTML");
    }

    @Test
    @DisplayName("fromBlock separates the heading level from unknown comment options")
    void fromBlock__succeeds__when_headingHasDeclaredLevel() {
        var source = block("<!-- wp:heading {\"level\":3,\"unknown\":true} --><h3>Title</h3><!-- /wp:heading -->");
        var model = adapter.fromBlock(source);
        assertThat(model.level()).isEqualTo(3);
        assertThat(model.contentHtml()).isEqualTo("Title");
        assertThat(model.attributes()).containsExactlyEntriesOf(Map.of("unknown", true));
        assertThat(model.source()).isSameAs(source);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 7})
    @DisplayName("toBlock rejects heading levels outside one through six")
    void toBlock__fails__when_levelIsOutsideRange(int level) {
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpHeadingBlock("x", level, Map.of())))
                                            .withMessage("Heading level must be 1 through 6");
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("nullText")
    @DisplayName("toBlock rejects null models and null heading text")
    void toBlock__fails__when_requiredModelDataIsNull(WpHeadingBlock model) {
        assertThatNullPointerException().isThrownBy(() -> adapter.toBlock(model));
    }

    @Test
    @DisplayName("toBlock rejects changes to existing heading options")
    void toBlock__fails__when_sourceOptionsChange() {
        var source = block("<!-- wp:heading --><h2>x</h2><!-- /wp:heading -->");
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpHeadingBlock("x", 2, Map.of("className", "new"), source)))
                                            .withMessageContaining("Changing saved block options");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    @DisplayName("toBlock creates every heading level and fromBlock recovers its rich text")
    void toBlock__succeeds__when_headingLevelIsValid(int level) {
        var result = adapter.toBlock(new WpHeadingBlock("Title <em>&amp; subtitle</em>", level,
                Map.of("className", "custom", "anchor", "intro", "style", Map.of("typography", Map.of("textAlign", "right")))));
        var heading = html(result).selectFirst("h" + level);
        assertThat(heading.html()).isEqualTo("Title <em>&amp; subtitle</em>");
        assertThat(heading.classNames()).containsExactlyInAnyOrder("wp-block-heading", "custom", "has-text-align-right");
        assertThat(heading.id()).isEqualTo("intro");
        assertThat(adapter.fromBlock(result).level()).isEqualTo(level);
        if (level == 2) assertThat(result.attributes()).doesNotContainKey("level");
        else assertThat(result.attributes()).containsEntry("level", level);
    }

    @Test
    @DisplayName("toBlock returns the original source when text and level are unchanged")
    void toBlock__succeeds__when_modelIsUnchanged() {
        var source = block("<!-- wp:heading --><h2 class='custom'>Title</h2><!-- /wp:heading -->");
        assertThat(adapter.toBlock(adapter.fromBlock(source))).isSameAs(source);
    }

    @ParameterizedTest
    @CsvSource({"New,2", "Title,2", "New,3"})
    @DisplayName("toBlock changes text or level while preserving the existing heading wrapper")
    void toBlock__succeeds__when_textOrLevelChanges(String text, int level) {
        var source = block("<!-- wp:heading {\"level\":3} --><h3 class='custom' data-test='x'>Title</h3><!-- /wp:heading -->");
        var model = adapter.fromBlock(source);
        var result = adapter.toBlock(new WpHeadingBlock(text, level, model.attributes(), source));
        var heading = html(result).selectFirst("h" + level);
        assertThat(heading.text()).isEqualTo(text);
        assertThat(heading.classNames()).containsExactly("custom");
        assertThat(heading.attr("data-test")).isEqualTo("x");
        if (level == 2) assertThat(result.attributes()).doesNotContainKey("level");
        else assertThat(result.attributes()).containsEntry("level", level);
        assertThat(html(source).selectFirst("h3").text()).isEqualTo("Title");
    }

    static Stream<WpHeadingBlock> nullText() {return Stream.of(new WpHeadingBlock(null, 2, Map.of()));}
}
