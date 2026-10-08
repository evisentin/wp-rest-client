package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Stored-markup fixtures exercise each named adapter through the public codec.
 */
@DisplayName("All registered adapter fixtures")
class AdapterExamplesTest {
    private static final DefaultWpBlockAdapterRegistry REGISTRY = new DefaultWpBlockAdapterRegistry();
    private final DefaultWpGutenbergCodec codec = new DefaultWpGutenbergCodec();

    @ParameterizedTest(name = "{0}: rejects another block type")
    @MethodSource("blockNames")
    @DisplayName("fromBlock rejects a foreign block name for every registered adapter")
    void fromBlock__fails__when_blockNameDoesNotMatch(String name) {
        var adapter = REGISTRY.findByBlockName(name).orElseThrow();
        WpBlock wrong = new WpBlock("vendor/unrelated", java.util.Map.of(), List.of(), WpBlockSyntax.PAIRED);
        assertThatThrownBy(() -> adapter.fromBlock(wrong)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toBlock preserves whitespace surrounding a saved block")
    void toBlock__succeeds__when_sourceHasSurroundingWhitespace() {
        assertStoredExampleRoundTrip("core/paragraph",
                " \r\n\t<!-- wp:paragraph --><p>Hello</p><!-- /wp:paragraph -->\r\n ");
    }

    @ParameterizedTest(name = "{0}: saved example round trip")
    @MethodSource("blockNames")
    @DisplayName("toBlock preserves the saved-markup fixture for every registered adapter")
    void toBlock__succeeds__when_usingStoredFixture(String name) throws Exception {
        String resource = "/gutenberg/adapter-examples/" + name.substring(5) + ".html";
        String raw;
        try (var input = Objects.requireNonNull(getClass().getResourceAsStream(resource), resource)) {
            raw = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertStoredExampleRoundTrip(name, raw);
    }

    private void assertStoredExampleRoundTrip(String name, String raw) {
        var document = codec.parse(raw);
        var blocks = document.nodes().stream().filter(WpBlock.class::isInstance).map(WpBlock.class::cast).toList();
        assertThat(blocks).hasSize(1);
        document.nodes().stream().filter(WpHtmlFragment.class::isInstance).map(WpHtmlFragment.class::cast)
                .forEach(fragment -> assertThat(fragment.html()).as("Whitespace outside the example block").isBlank());
        WpBlock block = blocks.getFirst();
        assertThat(block.name()).isEqualTo(name);
        var adapter = REGISTRY.findByBlockName(name).orElseThrow();
        WpBlock result = roundTrip(adapter, block);
        assertThat(result).isEqualTo(block);
        List<WpContentNode> roundTrippedNodes = document.nodes().stream()
                                                        .map(node -> node instanceof WpBlock ? result : node)
                                                        .toList();
        assertThat(codec.serialize(new WpBlockDocument(roundTrippedNodes))).isEqualTo(raw);
    }

    static Stream<String> blockNames() {
        return REGISTRY.blockNames().stream().sorted();
    }

    private static <T> WpBlock roundTrip(WpBlockAdapter<T> adapter, WpBlock block) {
        T model = adapter.fromBlock(block);
        assertThat(model).isInstanceOf(adapter.modelType());
        return adapter.toBlock(model);
    }
}
