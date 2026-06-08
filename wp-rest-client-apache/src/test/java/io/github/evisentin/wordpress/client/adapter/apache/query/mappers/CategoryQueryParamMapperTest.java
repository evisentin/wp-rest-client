package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.client.domain.model.enums.order.WpCategoryOrderFields;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

class CategoryQueryParamMapperTest extends ParamMapperTest {

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> CategoryQueryParamMapper.map(null, WpCategoryQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        CategoryQueryParamMapper.map(builder, null);

        assertThatUrlContainsNoQueryParam(builder);
    }

    @Test
    void maps_with_context() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withContext(WpContext.EDIT).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue()));
    }

    @Test
    void maps_with_default_values() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_hide_empty() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withHideEmpty(true).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        HIDE_EMPTY, "true"));
    }

    @Test
    void maps_with_include_ids() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_order() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withOrder(WpSortDirection.DESC).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue()));
    }

    @Test
    void maps_with_order_by() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withOrderBy(WpCategoryOrderFields.SLUG).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_parent_id() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withParentId(123456L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        PARENT, "123456"));
    }

    @Test
    void maps_with_post_id() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withPostId(123456L).build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        POST, "123456"));
    }

    @Test
    void maps_with_search() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withSearch("some criteria").build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slug() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withSlug("my-slug").build());

        assertThatUrlContainsExactly(builder,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCategoryOrderFields.NAME.getValue(),
                        SLUG, "my-slug"));
    }
}
