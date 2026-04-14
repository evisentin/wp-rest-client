package com.ev.wordpress.client.adapter.okhttp.query.mappers;

import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpSortDirection;
import com.ev.wordpress.client.domain.dto.enums.WpTagOrderFields;
import com.ev.wordpress.client.domain.dto.query.WpCategoryQuery;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.*;

class CategoryQueryParamMapperTest implements ParamMapperTest {

    private HttpUrl.Builder builder;

    @BeforeEach
    void beforeEach() {
        builder = Objects.requireNonNull(HttpUrl.parse("http://localhost")).newBuilder();
    }

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

        final HttpUrl url = builder.build();

        assertThatUrlContainsNoQueryParam(url);
    }

    @Test
    void maps_with_context() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withContext(WpContext.EDIT).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, "name"));
    }

    @Test
    void maps_with_default_values() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_hide_empty() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withHideEmpty(true).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        HIDE_EMPTY, "true"));
    }

    @Test
    void maps_with_include_ids() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_order() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withOrder(WpSortDirection.DESC).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, "name"));
    }

    @Test
    void maps_with_order_by() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withOrderBy(WpTagOrderFields.SLUG).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_parent_id() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withParentId(123456L).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        PARENT, "123456"));
    }

    @Test
    void maps_with_post_id() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withPostId(123456L).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        POST, "123456"));
    }

    @Test
    void maps_with_search() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withSearch("some criteria").build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slug() {
        CategoryQueryParamMapper.map(builder, WpCategoryQuery.builder().withSlug("my-slug").build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        SLUG, "my-slug"));
    }
}
