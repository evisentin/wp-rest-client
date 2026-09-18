package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.AdapterSupport;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("AdapterSupport")
class AdapterSupportTest {
    @Test
    @DisplayName("attributes rejects a null map")
    void attributes__fails__when_mapIsNull() {
        assertThatNullPointerException().isThrownBy(() -> AdapterSupport.attributes(null)).withMessage("attributes");
    }

    @Test
    @DisplayName("attributes creates a mutable shallow copy retaining null and unknown values")
    void attributes__succeeds__when_mapContainsNullsAndNestedValues() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("nullable", null);
        source.put("nested", new ArrayList<>(List.of("x")));
        var result = AdapterSupport.attributes(source);
        assertThat(result).isEqualTo(source).isNotSameAs(source);
        assertThat(result.get("nested")).isSameAs(source.get("nested"));
        result.put("new", true);
        assertThat(source).doesNotContainKey("new");
    }

    @Test
    @DisplayName("block creates paired literal content and copies attributes")
    void block__succeeds__when_savedHtmlIsSupplied() {
        var attributes = new LinkedHashMap<String, Object>(Map.of("unknown", true));
        var result = AdapterSupport.block("core/paragraph", attributes, "<p>x</p>");
        assertThat(result).isEqualTo(new WpBlock("core/paragraph", attributes, List.of(new WpHtmlFragment("<p>x</p>")), WpBlockSyntax.PAIRED));
        assertThat(result.attributes()).isNotSameAs(attributes);
    }

    @Test
    @DisplayName("check rejects a null block")
    void check__fails__when_blockIsNull() {
        assertThatNullPointerException().isThrownBy(() -> AdapterSupport.check(null, "core/group")).withMessage("block");
    }

    @ParameterizedTest
    @MethodSource("invalidBlocks")
    @DisplayName("check rejects a different name or nonpaired syntax")
    void check__fails__when_nameOrSyntaxDoesNotMatch(WpBlock block) {
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.check(block, "core/group"))
                                            .withMessage("Expected paired core/group block");
    }

    @Test
    @DisplayName("check accepts the expected paired block")
    void check__succeeds__when_nameAndSyntaxMatch() {
        assertThatCode(() -> AdapterSupport.check(new WpBlock("core/group", Map.of(), List.of(), WpBlockSyntax.PAIRED), "core/group"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    @DisplayName("common ignores missing and blank class names")
    void common__succeeds__when_classNameIsBlank(String className) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("className", className);
        var element = new Element("p").addClass("existing");
        AdapterSupport.common(element, attributes);
        assertThat(element.classNames()).containsExactly("existing");
        assertThat(element.hasAttr("id")).isFalse();
    }

    @Test
    @DisplayName("common applies classes and an anchor without removing existing classes")
    void common__succeeds__when_optionsArePresent() {
        var element = new Element("p").addClass("existing");
        AdapterSupport.common(element, Map.of("className", "first second", "anchor", "intro"));
        assertThat(element.classNames()).containsExactlyInAnyOrder("existing", "first", "second");
        assertThat(element.id()).isEqualTo("intro");
    }

    @Test
    @DisplayName("document parses body markup without pretty printing or sanitizing it")
    void document__succeeds__when_htmlContainsInlineMarkup() {
        var result = AdapterSupport.document("<p>Hello <strong>world</strong></p><script>example()</script>");
        assertThat(result.outputSettings().prettyPrint()).isFalse();
        assertThat(result.body().html()).isEqualTo("<p>Hello <strong>world</strong></p><script>example()</script>");
    }

    @Test
    @DisplayName("html rejects nested blocks instead of silently losing them")
    void html__fails__when_contentContainsNestedBlocks() {
        var child = new WpBlock("core/image", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING);
        var source = new WpBlock("core/paragraph", Map.of(), List.of(child), WpBlockSyntax.PAIRED);
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.html(source)).withMessage("Expected leaf HTML content in core/paragraph");
    }

    @Test
    @DisplayName("html concatenates leaf fragments in order")
    void html__succeeds__when_contentContainsOnlyFragments() {
        var source = new WpBlock("core/paragraph", Map.of(), List.of(new WpHtmlFragment("<p>"), new WpHtmlFragment("x</p>")), WpBlockSyntax.PAIRED);
        assertThat(AdapterSupport.html(source)).isEqualTo("<p>x</p>");
    }

    @Test
    @DisplayName("newAttributes identifies unsupported options")
    void newAttributes__fails__when_optionIsUnsupported() {
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.newAttributes(Map.of("unknown", true), Set.of("anchor")))
                                            .withMessage("Unsupported option for new block: unknown");
    }

    @Test
    @DisplayName("newAttributes accepts supported option names")
    void newAttributes__succeeds__when_allOptionsAreSupported() {
        assertThatCode(() -> AdapterSupport.newAttributes(Map.of("anchor", "intro"), Set.of("anchor"))).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "<p>x</p><p>y</p>", "<div>x</div>"})
    @DisplayName("root rejects missing, multiple and mismatched root elements")
    void root__fails__when_bodyDoesNotHaveOneMatchingElement(String html) {
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.root(AdapterSupport.document(html), "p"))
                                            .withMessage("Expected one p root element");
    }

    @Test
    @DisplayName("root returns the single matching root element")
    void root__succeeds__when_bodyHasOneMatchingElement() {
        var document = AdapterSupport.document("<p>x</p>");
        assertThat(AdapterSupport.root(document, "p")).isSameAs(document.body().child(0));
    }

    @ParameterizedTest
    @MethodSource("invalidAlignments")
    @DisplayName("textAlign rejects nonstring and unsupported alignment values")
    void textAlign__fails__when_alignmentValueIsInvalid(Object value) {
        var typography = new HashMap<String, Object>();
        typography.put("textAlign", value);
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.textAlign(new Element("p"), Map.of("style", Map.of("typography", typography))))
                                            .withMessage("Invalid text alignment");
    }

    @ParameterizedTest
    @MethodSource("invalidStyles")
    @DisplayName("textAlign rejects unsupported style shapes")
    void textAlign__fails__when_styleStructureIsUnsupported(Object style) {
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.textAlign(new Element("p"), Map.of("style", style)))
                                            .withMessage("New blocks support only style.typography.textAlign");
    }

    @ParameterizedTest
    @ValueSource(strings = {"left", "center", "right"})
    @DisplayName("textAlign applies each supported alignment")
    void textAlign__succeeds__when_alignmentIsSupported(String align) {
        var element = new Element("p");
        assertThat(AdapterSupport.textAlign(element, Map.of("style", Map.of("typography", Map.of("textAlign", align))))).isEqualTo(align);
        assertThat(element.classNames()).containsExactly("has-text-align-" + align);
    }

    @Test
    @DisplayName("textAlign leaves the element unchanged when style is absent")
    void textAlign__succeeds__when_styleIsAbsent() {
        var element = new Element("p");
        assertThat(AdapterSupport.textAlign(element, Map.of())).isNull();
        assertThat(element.classNames()).isEmpty();
    }

    @Test
    @DisplayName("unchangedAttributes rejects edits requiring regenerated markup")
    void unchangedAttributes__fails__when_optionsDiffer() {
        assertThatIllegalArgumentException().isThrownBy(() -> AdapterSupport.unchangedAttributes(Map.of("custom", true), Map.of()))
                                            .withMessageContaining("Changing saved block options requires regenerating their markup");
    }

    @Test
    @DisplayName("unchangedAttributes accepts equal option maps")
    void unchangedAttributes__succeeds__when_optionsAreEqual() {
        assertThatCode(() -> AdapterSupport.unchangedAttributes(new HashMap<>(Map.of("custom", true)), Map.of("custom", true)))
                .doesNotThrowAnyException();
    }

    static Stream<Arguments> invalidAlignments() {return Stream.of(Arguments.of((Object) null), Arguments.of(4), Arguments.of("justify"));}

    static Stream<WpBlock> invalidBlocks() {
        return Stream.of(new WpBlock("core/other", Map.of(), List.of(), WpBlockSyntax.PAIRED),
                new WpBlock("core/group", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING),
                new WpBlock("core/group", Map.of(), List.of(), null));
    }

    static Stream<Object> invalidStyles() {
        return Stream.of("bad", Map.of(), Map.of("typography", "bad"), Map.of("typography", Map.of()),
                Map.of("typography", Map.of("textAlign", "left", "fontSize", 12)));
    }
}
