package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Fluent model builders")
class ModelBuildersTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("modelsWithAttributes")
    @DisplayName("attributes rejects an explicitly null map while omitted maps default to empty")
    void attributes__fails__when_mapIsNull(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        assertThatThrownBy(() -> builder.getClass().getMethod("attributes", Map.class).invoke(builder, (Object) null))
                .isInstanceOf(InvocationTargetException.class).hasCauseInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelTypes")
    @DisplayName("build preserves explicitly supplied values for every record component")
    void build__succeeds__when_allComponentsAreSupplied(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        RecordComponent[] components = modelType.getRecordComponents();
        Object[] values = new Object[components.length];
        for (int index = 0; index < components.length; index++) {
            RecordComponent component = components[index];
            Object value = componentValue(component.getType());
            values[index] = value;
            Class<?> setterType = component.getType() == List.class ? Collection.class : component.getType();
            assertThat(builder.getClass().getMethod(component.getName(), setterType).invoke(builder, value))
                    .isSameAs(builder);
        }
        Class<?>[] parameterTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
        Object expected = modelType.getConstructor(parameterTypes).newInstance(values);
        assertThat(builder.getClass().getMethod("build").invoke(builder)).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelsWithAttributes")
    @DisplayName("build copies attribute maps, preserves null values and isolates subsequent builder changes")
    void build__succeeds__when_attributesAreAddedFluently(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        Class<?> builderType = builder.getClass();
        Map<String, Object> attributes = new LinkedHashMap<>();
        var nested = new ArrayList<>(List.of("shared"));
        attributes.put("nested", nested);
        attributes.put("nullable", null);
        builderType.getMethod("attributes", Map.class).invoke(builder, attributes);
        builderType.getMethod("attribute", String.class, Object.class).invoke(builder, "custom", true);
        Object model = builderType.getMethod("build").invoke(builder);
        Map<?, ?> actual = (Map<?, ?>) modelType.getMethod("attributes").invoke(model);
        Map<String, Object> expected = new LinkedHashMap<>(attributes);
        expected.put("custom", true);
        assertThat(actual).isEqualTo(expected).isNotSameAs(attributes);
        assertThat(actual.get("nested")).isSameAs(nested);
        assertThatThrownBy(actual::clear).isInstanceOf(UnsupportedOperationException.class);

        attributes.clear();
        builderType.getMethod("attribute", String.class, Object.class).invoke(builder, "later", 1);
        assertThat(actual).isEqualTo(expected);
        builderType.getMethod("clearAttributes").invoke(builder);
        Object cleared = builderType.getMethod("build").invoke(builder);
        assertThat((Map<?, ?>) modelType.getMethod("attributes").invoke(cleared)).isEmpty();
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelTypes")
    @DisplayName("build supplies empty collections for every omitted map and list component")
    void build__succeeds__when_collectionsAreOmitted(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        Object model = builder.getClass().getMethod("build").invoke(builder);
        assertThat(model).isInstanceOf(modelType);
        for (RecordComponent component : modelType.getRecordComponents()) {
            if (component.getType() == Map.class) {
                assertThat((Map<?, ?>) component.getAccessor().invoke(model)).isNotNull().isEmpty();
            } else if (component.getType() == List.class) {
                assertThat((List<?>) component.getAccessor().invoke(model)).isNotNull().isEmpty();
            }
        }
    }

    @Test
    @DisplayName("build supports generic blocks with incremental and bulk attributes")
    void build__succeeds__when_creatingAGenericBlock() {
        var fragment = WpHtmlFragment.builder().html("<p>Hello</p>").build();
        var block = WpBlock.builder()
                           .name("vendor/example")
                           .attribute("enabled", true)
                           .attributes(Map.of("count", 2))
                           .contentNode(fragment)
                           .syntax(WpBlockSyntax.PAIRED)
                           .build();
        assertThat(block).isEqualTo(new WpBlock("vendor/example", Map.of("enabled", true, "count", 2),
                List.of(fragment), WpBlockSyntax.PAIRED));
    }

    @Test
    @DisplayName("build supports a fluent paragraph-to-document workflow with default attributes")
    void build__succeeds__when_creatingAParagraphDocument() {
        var paragraph = WpParagraphBlock.builder().contentHtml("Hello <strong>world</strong>").build();
        var adapter = new io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.ParagraphBlockAdapter();
        var document = WpBlockDocument.builder().node(adapter.toBlock(paragraph)).build();
        assertThat(new DefaultWpGutenbergCodec().serialize(document))
                .isEqualTo("<!-- wp:paragraph --><p>Hello <strong>world</strong></p><!-- /wp:paragraph -->");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelsWithLists")
    @DisplayName("build supports a single fluent list entry")
    void build__succeeds__when_listHasOneEntry(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        RecordComponent component = Arrays.stream(modelType.getRecordComponents())
                                          .filter(item -> item.getType() == List.class).findFirst().orElseThrow();
        String singular = component.getName().equals("nodes") ? "node" : "contentNode";
        var fragment = WpHtmlFragment.builder().html("example").build();
        builder.getClass().getMethod(singular, WpContentNode.class).invoke(builder, fragment);
        Object model = builder.getClass().getMethod("build").invoke(builder);
        assertThat((List<?>) component.getAccessor().invoke(model)).isEqualTo(List.of(fragment));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelsWithLists")
    @DisplayName("build preserves list order and isolates previously built lists from builder changes")
    void build__succeeds__when_listItemsAreAddedFluently(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        Class<?> builderType = builder.getClass();
        RecordComponent component = Arrays.stream(modelType.getRecordComponents())
                                          .filter(item -> item.getType() == List.class).findFirst().orElseThrow();
        String singular = component.getName().equals("nodes") ? "node" : "contentNode";
        String clear = component.getName().equals("nodes") ? "clearNodes" : "clearContent";
        var first = new WpHtmlFragment("<div>");
        var child = WpBlock.builder().name("vendor/child").syntax(WpBlockSyntax.SELF_CLOSING).build();
        var last = new WpHtmlFragment("</div>");
        List<WpContentNode> supplied = new ArrayList<>(List.of(child, last));
        builderType.getMethod(singular, WpContentNode.class).invoke(builder, first);
        builderType.getMethod(component.getName(), Collection.class).invoke(builder, supplied);
        Object model = builderType.getMethod("build").invoke(builder);
        List<?> actual = (List<?>) component.getAccessor().invoke(model);
        assertThat(actual).isEqualTo(List.of(first, child, last)).isNotSameAs(supplied);
        assertThat(actual.get(1)).isSameAs(child);
        assertThatThrownBy(actual::clear).isInstanceOf(UnsupportedOperationException.class);
        supplied.clear();
        builderType.getMethod(singular, WpContentNode.class).invoke(builder, new WpHtmlFragment("later"));
        assertThat(actual).isEqualTo(List.of(first, child, last));
        builderType.getMethod(clear).invoke(builder);
        Object cleared = builderType.getMethod("build").invoke(builder);
        assertThat((List<?>) component.getAccessor().invoke(cleared)).isEmpty();
        assertThat(actual).isEqualTo(List.of(first, child, last));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("modelsWithLists")
    @DisplayName("list setters reject explicitly null collections")
    void builder__fails__when_listIsNull(Class<?> modelType) throws Exception {
        Object builder = modelType.getMethod("builder").invoke(null);
        RecordComponent component = Arrays.stream(modelType.getRecordComponents())
                                          .filter(item -> item.getType() == List.class).findFirst().orElseThrow();
        assertThatThrownBy(() -> builder.getClass().getMethod(component.getName(), Collection.class)
                                        .invoke(builder, (Object) null))
                .isInstanceOf(InvocationTargetException.class).hasCauseInstanceOf(NullPointerException.class);
    }

    static Stream<Class<?>> modelTypes() {
        var registry = new DefaultWpBlockAdapterRegistry();
        Stream<Class<?>> namedModels = registry.blockNames().stream()
                                               .map(name -> registry.findByBlockName(name).orElseThrow().modelType());
        return Stream.concat(Stream.of(WpBlock.class, WpBlockDocument.class, WpHtmlFragment.class), namedModels)
                     .distinct().sorted(Comparator.comparing(Class::getName));
    }

    static Stream<Class<?>> modelsWithAttributes() {
        return modelTypes().filter(type -> Arrays.stream(type.getRecordComponents())
                                                 .anyMatch(component -> component.getName().equals("attributes")));
    }

    static Stream<Class<?>> modelsWithLists() {
        return modelTypes().filter(type -> Arrays.stream(type.getRecordComponents())
                                                 .anyMatch(component -> component.getType() == List.class));
    }

    private static Object componentValue(Class<?> type) {
        if (type == String.class) return "example";
        if (type == int.class) return 3;
        if (type == Long.class) return 42L;
        if (type == Map.class) return Map.of("custom", true);
        if (type == List.class) return List.of(new WpHtmlFragment("<p>example</p>"));
        if (type == WpBlockSyntax.class) return WpBlockSyntax.PAIRED;
        if (type == WpBlock.class) return new WpBlock("core/paragraph", Map.of(), List.of(), WpBlockSyntax.PAIRED);
        throw new IllegalArgumentException("Add a sample value for record component type " + type.getName());
    }
}
