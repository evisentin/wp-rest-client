package io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPageOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.*;

class PageQueryParamMapperTest extends ParamMapperTest {

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> PageQueryParamMapper.map(null, WpPageQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        PageQueryParamMapper.map(builder, null);

        assertThatUrlContainsNoQueryParam(builder);
    }

    @Test
    void maps_with_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PageQueryParamMapper.map(builder, WpPageQuery.builder().withAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_author_exclude_ids() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withExcludeAuthorIds(Set.of(1000L, 1100L, 2000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        AUTHOR_EXCLUDE, "1000,1100,2000"));
    }

    @Test
    void maps_with_author_id() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withAuthorId(1000L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        AUTHOR, "1000"));
    }

    @Test
    void maps_with_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PageQueryParamMapper.map(builder, WpPageQuery.builder().withBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_context() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withContext(WpContext.EDIT).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_default_values() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_exclude_parent_ids() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withExcludeParentIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        PARENT_EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_include_ids() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_menu_order() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withMenuOrder(2L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        MENU_ORDER, "2"));
    }

    @Test
    void maps_with_modified_after() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PageQueryParamMapper.map(builder, WpPageQuery.builder().withModifiedAfter(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        MODIFIED_AFTER, "2026-01-01"));
    }

    @Test
    void maps_with_modified_before() {

        LocalDate date = LocalDate.of(2026, 1, 1);

        PageQueryParamMapper.map(builder, WpPageQuery.builder().withModifiedBefore(date).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        MODIFIED_BEFORE, "2026-01-01"));
    }

    @Test
    void maps_with_order() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withOrder(WpSortDirection.DESC).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_order_by() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withOrderBy(WpPageOrderFields.SLUG).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_parent_id() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withParent(12345L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        PARENT, "12345"));
    }

    @Test
    void maps_with_search() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withSearch("some criteria").build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slugs() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withSlugs(Set.of("a", "b", "c")).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        SLUG, "a,b,c"));
    }

    @Test
    void maps_with_statuses() {
        PageQueryParamMapper.map(builder, WpPageQuery.builder().withStatuses(Set.of(WpPageStatus.TRASH, WpPageStatus.DRAFT)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPageOrderFields.DATE.getValue(),
                        STATUS, "draft,trash"));
    }
}
