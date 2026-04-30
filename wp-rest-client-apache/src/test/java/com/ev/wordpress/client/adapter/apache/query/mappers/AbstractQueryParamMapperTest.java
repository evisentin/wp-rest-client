package com.ev.wordpress.client.adapter.apache.query.mappers;

import com.ev.wordpress.client.domain.model.enums.WpHasValueEnum;
import lombok.NonNull;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

class AbstractQueryParamMapperTest extends ParamMapperTest {

    @Test
    void addBoolean_adds_value_when_not_null() {
        TestMapper.addBooleanParam(builder, "hide_empty", true);

        assertThat(builderQueryParameter("hide_empty")).isEqualTo("true");
    }

    @Test
    void addBoolean_does_nothing_when_null() {
        TestMapper.addBooleanParam(builder, "hide_empty", null);

        assertThat(builderQueryParameter("hide_empty")).isNull();
    }

    @Test
    void addEnum_adds_value_when_not_null() {
        TestMapper.addEnumParam(builder, "context", TestEnum.EDIT);

        assertThat(builderQueryParameter("context"))
                .isEqualTo("edit");
    }

    @Test
    void addEnum_does_nothing_when_null() {
        TestMapper.addEnumParam(builder, "context", null);

        assertThat(builderQueryParameter("context")).isNull();
    }

    @Test
    void addInteger_adds_value_when_not_null() {
        TestMapper.addIntegerParam(builder, "page", 42);

        assertThat(builderQueryParameter("page"))
                .isEqualTo("42");
    }

    @Test
    void addInteger_does_nothing_when_null() {
        TestMapper.addIntegerParam(builder, "page", null);

        assertThat(builderQueryParameter("page")).isNull();
    }

    @Test
    void addLocalDate_does_nothing_when_null() {
        TestMapper.addLocalDateParam(builder, "after", null);

        assertThat(builderQueryParameter("after")).isNull();
    }

    @Test
    void addLocalDate_formats_as_iso_local_date() {
        TestMapper.addLocalDateParam(builder, "after", LocalDate.of(2026, 4, 9));

        assertThat(builderQueryParameter("after"))
                .isEqualTo("2026-04-09");
    }

    @Test
    void addLong_adds_value_when_not_null() {
        TestMapper.addLongParam(builder, "parent", 123456L);

        assertThat(builderQueryParameter("parent"))
                .isEqualTo("123456");
    }

    @Test
    void addLong_does_nothing_when_null() {
        TestMapper.addLongParam(builder, "parent", null);

        assertThat(builderQueryParameter("parent")).isNull();
    }

    @Test
    void addSetOfEnums_does_nothing_when_null() {
        TestMapper.addSetOfEnumsParam(builder, "status", null);

        assertThat(builderQueryParameter("status")).isNull();
    }

    @Test
    void addSetOfEnums_sorts_and_adds_each_value() {
        TestMapper.addSetOfEnumsParam(builder, "status", Set.of(TestEnum.PUBLISH, TestEnum.DRAFT, TestEnum.EDIT));

        assertThat(builderQueryParameter("status"))
                .isEqualTo("draft,edit,publish");
    }

    @Test
    void addSetOfLong_does_nothing_when_null() {
        TestMapper.addSetOfLongParam(builder, "include", null);

        assertThat(builderQueryParameter("include")).isNull();
    }

    @Test
    void addSetOfLong_sorts_and_adds_each_value() {
        TestMapper.addSetOfLongParam(builder, "include", Set.of(3000L, 1000L, 2000L));

        assertThat(builderQueryParameter("include"))
                .isEqualTo("1000,2000,3000");
    }

    @Test
    void addSetOfStrings_does_nothing_when_null() {
        TestMapper.addSetOfStringsParam(builder, "slug", null);

        assertThat(builderQueryParameter("slug")).isNull();
    }

    @Test
    void addSetOfStrings_trims_filters_blanks_sorts_and_deduplicates() {
        TestMapper.addSetOfStringsParam(builder, "slug", Set.of("  beta ", "alpha", " ", "beta"));

        assertThat(builderQueryParameter("slug"))
                .isEqualTo("alpha,beta");
    }

    @Test
    void addString_adds_trimmed_value_when_not_blank() {
        TestMapper.addStringParam(builder, "search", "  some criteria  ");

        assertThat(builderQueryParameter("search"))
                .isEqualTo("some criteria");
    }

    @Test
    void addString_does_nothing_when_blank() {
        TestMapper.addStringParam(builder, "search", "   ");

        assertThat(builderQueryParameter("search")).isNull();
    }

    @Test
    void addString_does_nothing_when_null() {
        TestMapper.addStringParam(builder, "search", null);

        assertThat(builderQueryParameter("search")).isNull();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void addString_fails_on_null_builder() {
        assertThatThrownBy(() -> TestMapper.addStringParam(null, "search", "value"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    private String builderQueryParameter(String key) {
        assertThatNoException().isThrownBy(() -> builder.build());
        return builder.getQueryParams().stream()
                      .collect(toMap(NameValuePair::getName, NameValuePair::getValue))
                      .get(key);
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

        static void addBooleanParam(@NonNull URIBuilder builder, String name, Boolean value) {
            addBoolean(builder, name, value);
        }

        static void addEnumParam(@NonNull URIBuilder builder, String name, WpHasValueEnum value) {
            addEnum(builder, name, value);
        }

        static void addIntegerParam(@NonNull URIBuilder builder, String name, Integer value) {
            addInteger(builder, name, value);
        }

        static void addLocalDateParam(@NonNull URIBuilder builder, String name, LocalDate value) {
            addLocalDate(builder, name, value);
        }

        static void addLongParam(@NonNull URIBuilder builder, String name, Long value) {
            addLong(builder, name, value);
        }

        static void addSetOfEnumsParam(@NonNull URIBuilder builder, String name, Set<? extends WpHasValueEnum> values) {
            addSetOfEnums(builder, name, values);
        }

        static void addSetOfLongParam(@NonNull URIBuilder builder, String name, Set<Long> values) {
            addSetOfLong(builder, name, values);
        }

        static void addSetOfStringsParam(@NonNull URIBuilder builder, String name, Set<String> values) {
            addSetOfStrings(builder, name, values);
        }

        static void addStringParam(@NonNull URIBuilder builder, String name, String value) {
            addString(builder, name, value);
        }
    }
}
