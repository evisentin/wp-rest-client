package io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPostRevisionOrderFields;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostRevisionQuery;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.*;

class PostRevisionQueryParamMapperTest implements ParamMapperTest {

    private HttpUrl.Builder builder;

    @BeforeEach
    void beforeEach() {
        builder = Objects.requireNonNull(HttpUrl.parse("http://localhost")).newBuilder();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> PostRevisionQueryParamMapper.map(null, WpPostRevisionQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        PostQueryParamMapper.map(builder, null);

        final HttpUrl url = builder.build();

        assertThatUrlContainsNoQueryParam(url);
    }

    @Test
    void maps_with_context() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withContext(WpContext.EDIT).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_default_values() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_include_ids() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_order() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withOrder(WpSortDirection.DESC).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue()));
    }

    @Test
    void maps_with_order_by() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withOrderBy(WpPostRevisionOrderFields.SLUG).build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.SLUG.getValue()));
    }

    @Test
    void maps_with_search() {
        PostRevisionQueryParamMapper.map(builder, WpPostRevisionQuery.builder().withSearch("some criteria").build());

        final HttpUrl url = builder.build();

        assertThatUrlContainsExactly(url,
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpPostRevisionOrderFields.DATE.getValue(),
                        SEARCH, "some criteria"));
    }
}
