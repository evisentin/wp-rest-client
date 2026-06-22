package io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpMediaOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpMediaQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.*;

class MediaQueryParamMapperTest extends ParamMapperTest {

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> MediaQueryParamMapper.map(null, WpMediaQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        MediaQueryParamMapper.map(builder, null);

        assertThatUrlContainsNoQueryParam(builder);
    }

    @Test
    void maps_with_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_author_exclude_ids() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withExcludeAuthorIds(Set.of(1000L, 1100L, 2000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        AUTHOR_EXCLUDE, "1000,1100,2000"));
    }

    @Test
    void maps_with_author_id() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withAuthorId(1000L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        AUTHOR, "1000"));
    }

    @Test
    void maps_with_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_context() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withContext(WpContext.EDIT).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue()));
    }

    @Test
    void maps_with_default_values() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_include_ids() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_modified_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withModifiedAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        MODIFIED_AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_modified_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withModifiedBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        MODIFIED_BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_order() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withOrder(WpSortDirection.DESC).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue()));
    }

    @Test
    void maps_with_order_by() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withOrderBy(WpMediaOrderFields.ID).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue()));
    }

    @Test
    void maps_with_search() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withSearch("some criteria").build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slugs() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withSlugs(Set.of("a", "b", "c")).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        SLUG, "a,b,c"));
    }

    @Test
    void maps_with_statuses() {
        MediaQueryParamMapper.map(builder, WpMediaQuery.builder().withStatuses(Set.of(WpMediaStatus.TRASH, WpMediaStatus.DRAFT)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpMediaOrderFields.ID.getValue(),
                        STATUS, "draft,trash"));
    }
}
