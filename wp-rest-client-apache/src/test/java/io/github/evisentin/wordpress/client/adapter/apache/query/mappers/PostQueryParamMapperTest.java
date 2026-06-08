package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.client.domain.model.enums.order.WpPostOrderFields;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

class PostQueryParamMapperTest extends ParamMapperTest {

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> PostQueryParamMapper.map(null, WpPostQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        PostQueryParamMapper.map(builder, null);

        assertThatUrlContainsNoQueryParam(builder);
    }

    @Test
    void maps_with_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PostQueryParamMapper.map(builder, WpPostQuery.builder().withAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_author_exclude_ids() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withExcludeAuthorIds(Set.of(1000L, 1100L, 2000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        AUTHOR_EXCLUDE, "1000,1100,2000"));
    }

    @Test
    void maps_with_author_id() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withAuthorId(1000L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        AUTHOR, "1000"));
    }

    @Test
    void maps_with_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PostQueryParamMapper.map(builder, WpPostQuery.builder().withBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_context() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withContext(WpContext.EDIT).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_default_values() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_exclude_categories() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withExcludeCategories(Set.of(300L, 100L, 200L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        CATEGORIES_EXCLUDE, "100,200,300"));
    }

    @Test
    void maps_with_exclude_ids() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_exclude_tags() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withExcludeTags(Set.of(300L, 100L, 200L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        TAGS_EXCLUDE, "100,200,300"));
    }

    @Test
    void maps_with_include_categories() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withIncludeCategories(Set.of(300L, 100L, 200L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        CATEGORIES, "100,200,300"));
    }

    @Test
    void maps_with_include_ids() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_include_tags() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withIncludeTags(Set.of(300L, 100L, 200L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        TAGS, "100,200,300"));
    }

    @Test
    void maps_with_modified_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PostQueryParamMapper.map(builder, WpPostQuery.builder().withModifiedAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        MODIFIED_AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_modified_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PostQueryParamMapper.map(builder, WpPostQuery.builder().withModifiedBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        MODIFIED_BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_order() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withOrder(WpSortDirection.DESC).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_order_by() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withOrderBy(WpPostOrderFields.SLUG).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_search() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withSearch("some criteria").build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slugs() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withSlugs(Set.of("a", "b", "c")).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        SLUG, "a,b,c"));
    }

    @Test
    void maps_with_statuses() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withStatuses(Set.of(WpPostStatus.TRASH, WpPostStatus.DRAFT)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        STATUS, "draft,trash"));
    }

    @Test
    void maps_with_sticky() {
        PostQueryParamMapper.map(builder, WpPostQuery.builder().withSticky(Boolean.TRUE).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostOrderFields.DATE.getValue(),
                        STICKY, "true"));
    }
}
