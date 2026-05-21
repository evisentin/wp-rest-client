package io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTagOrderFields;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

class TagQueryParamMapperTest implements ParamMapperTest {

    private HttpUrl.Builder builder;

    @BeforeEach
    void beforeEach() {
        builder = Objects.requireNonNull(HttpUrl.parse("http://localhost")).newBuilder();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> TagQueryParamMapper.map(null, WpTagQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        TagQueryParamMapper.map(builder, null);

        final HttpUrl url = builder.build();

        assertThatUrlContainsNoQueryParam(url);
    }

    @Test
    void maps_with_context() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withContext(WpContext.EDIT).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        "order_by", "name"));
    }

    @Test
    void maps_with_default_values() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_hide_empty() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withHideEmpty(true).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        HIDE_EMPTY, "true"));
    }

    @Test
    void maps_with_include_ids() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_offset() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withOffset(13).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        OFFSET, "13"));
    }

    @Test
    void maps_with_order() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withOrder(WpSortDirection.DESC).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, "name"));
    }

    @Test
    void maps_with_order_by() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withOrderBy(WpTagOrderFields.SLUG).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_post_id() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withPostId(123456L).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        POST, "123456"));
    }

    @Test
    void maps_with_search() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withSearch("some criteria").build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_slug() {
        TagQueryParamMapper.map(builder, WpTagQuery.builder().withSlug("my-slug").build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpTagOrderFields.NAME.getValue(),
                        SLUG, "my-slug"));
    }
}
