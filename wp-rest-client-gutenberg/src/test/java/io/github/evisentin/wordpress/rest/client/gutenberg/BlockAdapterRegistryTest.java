package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.AudioBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.ParagraphBlockAdapter;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlockAdapterRegistryTest {
    private final DefaultWpBlockAdapterRegistry registry = new DefaultWpBlockAdapterRegistry();

    @Test
    void coversEveryDefinitionInWordPress71Release() throws Exception {
        try (var input = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                getClass().getResourceAsStream("/gutenberg/wordpress-7.1-block-names.txt")), StandardCharsets.UTF_8))) {
            Set<String> names = input.lines().filter(line -> !line.startsWith("#")).collect(Collectors.toSet());
            assertThat(names).hasSize(115);
            assertThat(registry.blockNames()).containsExactlyInAnyOrderElementsOf(names);
        }
    }

    @Test
    void everySavedContentAdapterPreservesNestedContentNullAttributesAndSyntax() {
        Set<String> specialized = Set.of("core/paragraph", "core/heading", "core/image", "core/group");
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("custom", Map.of("nested", Arrays.asList("text", 4, null)));
        attributes.put("nullable", null);
        var child = new WpBlock("vendor/unknown", Map.of(), List.of(), WpBlockSyntax.SELF_CLOSING);
        for (String name : registry.blockNames()) {
            if (specialized.contains(name)) {
                continue;
            }
            WpBlockAdapter<?> adapter = registry.findByBlockName(name).orElseThrow();
            var paired = new WpBlock(name, attributes,
                    List.of(new WpHtmlFragment("<div>"), child, new WpHtmlFragment("</div>")), WpBlockSyntax.PAIRED);
            assertThat(roundTrip(adapter, paired)).as(name).isEqualTo(paired);
            var selfClosing = new WpBlock(name, attributes, List.of(), WpBlockSyntax.SELF_CLOSING);
            assertThat(roundTrip(adapter, selfClosing)).as(name).isEqualTo(selfClosing);
            assertThat(registry.findByModelType(adapter.modelType()).orElseThrow()).isSameAs(adapter);
        }
    }

    @Test
    void providesTypeSafeLookupAndLeavesUnknownPluginBlocksGeneric() {
        WpBlockAdapter<WpParagraphBlock> adapter = registry.findByModelType(WpParagraphBlock.class).orElseThrow();
        assertThat(adapter).isInstanceOf(ParagraphBlockAdapter.class);
        assertThat(registry.findByBlockName("vendor/plugin")).isEmpty();
        assertThat(registry.findByModelType(String.class)).isEmpty();
    }

    @Test
    void rejectsDuplicateRegistrationsAndInvalidSavedContent() {
        assertThatThrownBy(() -> new DefaultWpBlockAdapterRegistry(List.of(new ParagraphBlockAdapter(), new ParagraphBlockAdapter())))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AudioBlockAdapter().fromBlock(new WpBlock("core/video", Map.of(), List.of(), WpBlockSyntax.PAIRED)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new AudioBlockAdapter().toBlock(new WpAudioBlock(Map.of(), List.of(new WpHtmlFragment("x")), WpBlockSyntax.SELF_CLOSING)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static <T> WpBlock roundTrip(WpBlockAdapter<T> adapter, WpBlock block) {
        return adapter.toBlock(adapter.fromBlock(block));
    }
}
