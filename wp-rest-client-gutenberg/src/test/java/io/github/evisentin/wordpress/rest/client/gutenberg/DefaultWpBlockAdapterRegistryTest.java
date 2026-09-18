package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.ParagraphBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DefaultWpBlockAdapterRegistry")
class DefaultWpBlockAdapterRegistryTest {
    private final DefaultWpBlockAdapterRegistry registry = new DefaultWpBlockAdapterRegistry();

    @Test
    @DisplayName("blockNames contains exactly the pinned 115-name catalog and is unmodifiable")
    void blockNames__succeeds__when_usingDefaultCatalog() throws Exception {
        try (var input = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                getClass().getResourceAsStream("/gutenberg/wordpress-7.1-block-names.txt")), StandardCharsets.UTF_8))) {
            var expected = input.lines().filter(line -> !line.startsWith("#")).toList();
            assertThat(expected).hasSize(115).doesNotHaveDuplicates();
            assertThat(registry.blockNames()).containsExactlyInAnyOrderElementsOf(expected);
            assertThatThrownBy(() -> registry.blockNames().clear()).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @ParameterizedTest
    @MethodSource("invalidCatalogs")
    @DisplayName("constructor rejects null catalogs, adapters, names and model classes")
    void constructor__fails__when_registrationContainsNulls(Collection<WpBlockAdapter<?>> adapters) {
        assertThatNullPointerException().isThrownBy(() -> new DefaultWpBlockAdapterRegistry(adapters));
    }

    @ParameterizedTest
    @ValueSource(strings = {"core/paragraph", "vendor/other"})
    @DisplayName("constructor rejects duplicate names or duplicate model classes")
    void constructor__fails__when_registrationIsDuplicated(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> new DefaultWpBlockAdapterRegistry(
                                                    List.of(new ParagraphBlockAdapter(), new DeclaredAdapter(name, WpParagraphBlock.class))))
                                            .withMessage("Duplicate adapter name or model type: " + name);
    }

    @Test
    @DisplayName("constructor accepts an empty catalog")
    void constructor__succeeds__when_catalogIsEmpty() {
        assertThat(new DefaultWpBlockAdapterRegistry(List.of()).blockNames()).isEmpty();
    }

    @Test
    @DisplayName("constructor snapshots only the explicitly supplied adapters")
    void constructor__succeeds__when_customCatalogIsSupplied() {
        var adapter = new ParagraphBlockAdapter();
        List<WpBlockAdapter<?>> selected = new ArrayList<>(List.of(adapter));
        var custom = new DefaultWpBlockAdapterRegistry(selected);
        selected.clear();
        assertThat(custom.blockNames()).containsExactly("core/paragraph");
        assertThat(custom.findByBlockName("core/paragraph")).containsSame(adapter);
        assertThat(custom.findByModelType(WpParagraphBlock.class)).containsSame(adapter);
        assertThat(custom.findByBlockName("core/image")).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"vendor/unknown", "paragraph", "CORE/paragraph", ""})
    @DisplayName("findByBlockName returns empty for unknown or noncanonical names")
    void findByBlockName__succeeds__when_nameIsNotRegistered(String name) {
        assertThat(registry.findByBlockName(name)).isEmpty();
    }

    @Test
    @DisplayName("findByModelType returns empty for an unregistered type")
    void findByModelType__succeeds__when_typeIsNotRegistered() {
        assertThat(registry.findByModelType(String.class)).isEmpty();
    }

    @Test
    @DisplayName("findByModelType returns the typed adapter for an exact class")
    void findByModelType__succeeds__when_typeIsRegistered() {
        assertThat(registry.findByModelType(WpParagraphBlock.class)).get().isInstanceOf(ParagraphBlockAdapter.class);
    }

    static Stream<Arguments> invalidCatalogs() {
        return Stream.of(Arguments.of((Object) null), Arguments.of(Arrays.asList((WpBlockAdapter<?>) null)),
                Arguments.of(List.of(new DeclaredAdapter(null, String.class))),
                Arguments.of(List.of(new DeclaredAdapter("vendor/other", null))));
    }

    private static final class DeclaredAdapter implements WpBlockAdapter<Object> {
        private final String name;
        private final Class<?> type;

        private DeclaredAdapter(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String blockName() {return name;}

        // Registration only uses metadata; these methods must not be called by the registry.
        @Override
        public Object fromBlock(WpBlock block) {throw new UnsupportedOperationException();}

        @Override
        @SuppressWarnings("unchecked")
        public Class<Object> modelType() {return (Class<Object>) type;}

        @Override
        public WpBlock toBlock(Object model) {throw new UnsupportedOperationException();}
    }
}
