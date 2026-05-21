package io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpHasValueEnum;
import lombok.NonNull;
import okhttp3.HttpUrl;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

class AbstractQueryParamMapperTest implements WithAssertions {

    private HttpUrl.Builder builder;

    @Test
    void addBoolean_adds_value_when_not_null() {
        TestMapper.addBooleanParam(builder, "hide_empty", true);

        assertThat(builder.build().queryParameterValues("hide_empty"))
                .containsExactly("true");
    }

    @Test
    void addBoolean_does_nothing_when_null() {
        TestMapper.addBooleanParam(builder, "hide_empty", null);

        assertThat(builder.build().queryParameter("hide_empty")).isNull();
    }

    @Test
    void addEnum_adds_value_when_not_null() {
        TestMapper.addEnumParam(builder, "context", TestEnum.EDIT);

        assertThat(builder.build().queryParameterValues("context"))
                .containsExactly("edit");
    }

    @Test
    void addEnum_does_nothing_when_null() {
        TestMapper.addEnumParam(builder, "context", null);

        assertThat(builder.build().queryParameter("context")).isNull();
    }

    @Test
    void addInteger_adds_value_when_not_null() {
        TestMapper.addIntegerParam(builder, "page", 42);

        assertThat(builder.build().queryParameterValues("page"))
                .containsExactly("42");
    }

    @Test
    void addInteger_does_nothing_when_null() {
        TestMapper.addIntegerParam(builder, "page", null);

        assertThat(builder.build().queryParameter("page")).isNull();
    }

    @Test
    void addLocalDate_does_nothing_when_null() {
        TestMapper.addLocalDateParam(builder, "after", null);

        assertThat(builder.build().queryParameter("after")).isNull();
    }

    @Test
    void addLocalDate_formats_as_iso_local_date() {
        TestMapper.addLocalDateParam(builder, "after", LocalDate.of(2026, 4, 9));

        assertThat(builder.build().queryParameterValues("after"))
                .containsExactly("2026-04-09");
    }

    @Test
    void addLong_adds_value_when_not_null() {
        TestMapper.addLongParam(builder, "parent", 123456L);

        assertThat(builder.build().queryParameterValues("parent"))
                .containsExactly("123456");
    }

    @Test
    void addLong_does_nothing_when_null() {
        TestMapper.addLongParam(builder, "parent", null);

        assertThat(builder.build().queryParameter("parent")).isNull();
    }

    @Test
    void addSetOfEnums_does_nothing_when_null() {
        TestMapper.addSetOfEnumsParam(builder, "status", null);

        assertThat(builder.build().queryParameterValues("status")).isEmpty();
    }

    @Test
    void addSetOfEnums_sorts_and_adds_each_value() {
        TestMapper.addSetOfEnumsParam(builder, "status", Set.of(TestEnum.PUBLISH, TestEnum.DRAFT, TestEnum.EDIT));

        assertThat(builder.build().queryParameterValues("status"))
                .containsExactly("draft,edit,publish");
    }

    @Test
    void addSetOfLong_does_nothing_when_null() {
        TestMapper.addSetOfLongParam(builder, "include", null);

        assertThat(builder.build().queryParameterValues("include")).isEmpty();
    }

    @Test
    void addSetOfLong_sorts_and_adds_each_value() {
        TestMapper.addSetOfLongParam(builder, "include", Set.of(3000L, 1000L, 2000L));

        assertThat(builder.build().queryParameterValues("include"))
                .containsExactly("1000,2000,3000");
    }

    @Test
    void addSetOfStrings_does_nothing_when_null() {
        TestMapper.addSetOfStringsParam(builder, "slug", null);

        assertThat(builder.build().queryParameterValues("slug")).isEmpty();
    }

    @Test
    void addSetOfStrings_trims_filters_blanks_sorts_and_deduplicates() {
        TestMapper.addSetOfStringsParam(builder, "slug", Set.of("  beta ", "alpha", " ", "beta"));

        assertThat(builder.build().queryParameterValues("slug"))
                .containsExactly("alpha,beta");
    }

    @Test
    void addString_adds_trimmed_value_when_not_blank() {
        TestMapper.addStringParam(builder, "search", "  some criteria  ");

        assertThat(builder.build().queryParameterValues("search"))
                .containsExactly("some criteria");
    }

    @Test
    void addString_does_nothing_when_blank() {
        TestMapper.addStringParam(builder, "search", "   ");

        assertThat(builder.build().queryParameter("search")).isNull();
    }

    @Test
    void addString_does_nothing_when_null() {
        TestMapper.addStringParam(builder, "search", null);

        assertThat(builder.build().queryParameter("search")).isNull();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void addString_fails_on_null_builder() {
        assertThatThrownBy(() -> TestMapper.addStringParam(null, "search", "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @BeforeEach
    void setUp() {
        builder = Objects.requireNonNull(HttpUrl.parse("http://localhost")).newBuilder();
    }

    private enum TestEnum implements WpHasValueEnum {
        DRAFT("draft"),
        EDIT("edit"),
        PUBLISH("publish");

        private final String value;

        TestEnum(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final class TestMapper extends AbstractQueryParamMapper {

        static void addBooleanParam(@NonNull HttpUrl.Builder builder, String name, Boolean value) {
            addBoolean(builder, name, value);
        }

        static void addEnumParam(@NonNull HttpUrl.Builder builder, String name, WpHasValueEnum value) {
            addEnum(builder, name, value);
        }

        static void addIntegerParam(@NonNull HttpUrl.Builder builder, String name, Integer value) {
            addInteger(builder, name, value);
        }

        static void addLocalDateParam(@NonNull HttpUrl.Builder builder, String name, LocalDate value) {
            addLocalDate(builder, name, value);
        }

        static void addLongParam(@NonNull HttpUrl.Builder builder, String name, Long value) {
            addLong(builder, name, value);
        }

        static void addSetOfEnumsParam(@NonNull HttpUrl.Builder builder, String name, Set<? extends WpHasValueEnum> values) {
            addSetOfEnums(builder, name, values);
        }

        static void addSetOfLongParam(@NonNull HttpUrl.Builder builder, String name, Set<Long> values) {
            addSetOfLong(builder, name, values);
        }

        static void addSetOfStringsParam(@NonNull HttpUrl.Builder builder, String name, Set<String> values) {
            addSetOfStrings(builder, name, values);
        }

        static void addStringParam(@NonNull HttpUrl.Builder builder, String name, String value) {
            addString(builder, name, value);
        }
    }
}
