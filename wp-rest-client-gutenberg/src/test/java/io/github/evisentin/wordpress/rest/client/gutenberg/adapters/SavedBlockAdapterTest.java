package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpBlockAdapterRegistry;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media.AudioBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpAudioBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SavedBlockAdapter and all named adapters")
class SavedBlockAdapterTest {
    private static final DefaultWpBlockAdapterRegistry REGISTRY = new DefaultWpBlockAdapterRegistry();
    private final AudioBlockAdapter adapter = new AudioBlockAdapter();

    @Test
    @DisplayName("fromBlock rejects a null block")
    void fromBlock__fails__when_blockIsNull() {
        assertThatNullPointerException().isThrownBy(() -> adapter.fromBlock(null)).withMessage("block");
    }

    @ParameterizedTest
    @MethodSource("invalidContent")
    @DisplayName("fromBlock rejects null content, null syntax and nonempty self-closing blocks")
    void fromBlock__fails__when_savedContentIsInvalid(List<WpContentNode> content, WpBlockSyntax syntax, Class<? extends Throwable> error) {
        assertThatThrownBy(() -> adapter.fromBlock(new WpBlock("core/audio", Map.of(), content, syntax))).isInstanceOf(error);
    }

    @Test
    @DisplayName("toBlock rejects a null model")
    void toBlock__fails__when_modelIsNull() {
        assertThatNullPointerException().isThrownBy(() -> adapter.toBlock(null)).withMessage("model");
    }

    @ParameterizedTest
    @MethodSource("invalidContent")
    @DisplayName("toBlock rejects null content, null syntax and nonempty self-closing models")
    void toBlock__fails__when_savedContentIsInvalid(List<WpContentNode> content, WpBlockSyntax syntax, Class<? extends Throwable> error) {
        assertThatThrownBy(() -> adapter.toBlock(new WpAudioBlock(Map.of(), content, syntax))).isInstanceOf(error);
    }

    @ParameterizedTest(name = "{0}: {1}")
    @MethodSource("savedBlocks")
    @DisplayName("toBlock preserves attributes, wrapper positions and syntax for every saved-content model")
    void toBlock__succeeds__when_namedModelContainsSavedContent(String name, WpBlockSyntax syntax) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("custom", Map.of("nested", Arrays.asList("text", 4, null)));
        attributes.put("nullable", null);
        var child = new WpBlock("vendor/unknown", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING);
        List<WpContentNode> content = syntax == WpBlockSyntax.PAIRED
                ? new ArrayList<>(List.of(new WpHtmlFragment("<div>"), child, new WpHtmlFragment("</div>"))) : new ArrayList<>();
        var source = new WpBlock(name, attributes, content, syntax);
        var registered = REGISTRY.findByBlockName(name).orElseThrow();
        WpBlock result = roundTrip(registered, source);
        assertThat(result).isEqualTo(source);
        assertThat(result.attributes()).isNotSameAs(attributes);
        assertThat(result.content()).isNotSameAs(content);
        assertThat(result.attributes().get("custom")).isSameAs(attributes.get("custom"));
        assertThatThrownBy(() -> result.content().clear()).isInstanceOf(UnsupportedOperationException.class);
        attributes.put("later", true);
        content.clear();
        assertThat(result.attributes()).doesNotContainKey("later");
        assertThat(result.content()).hasSize(syntax == WpBlockSyntax.PAIRED ? 3 : 0);
        assertThat(REGISTRY.findByModelType(registered.modelType())).get().isSameAs(registered);
    }

    static Stream<Arguments> invalidContent() {
        return Stream.of(Arguments.of(null, WpBlockSyntax.PAIRED, NullPointerException.class),
                Arguments.of(List.of(), null, NullPointerException.class),
                Arguments.of(List.of(new WpHtmlFragment("x")), WpBlockSyntax.SELF_CLOSING, IllegalArgumentException.class));
    }

    static Stream<Arguments> savedBlocks() {
        return REGISTRY.blockNames().stream().sorted()
                       .filter(name -> !Set.of("core/paragraph", "core/heading", "core/image", "core/group").contains(name))
                       .flatMap(name -> Stream.of(WpBlockSyntax.values()).map(syntax -> Arguments.of(name, syntax)));
    }

    private static <T> WpBlock roundTrip(WpBlockAdapter<T> adapter, WpBlock source) {
        T model = adapter.fromBlock(source);
        assertThat(model).isInstanceOf(adapter.modelType());
        return adapter.toBlock(model);
    }
}
