package io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpCommentStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.client.domain.model.enums.order.WpCommentOrderFields;
import io.github.evisentin.wordpress.client.domain.model.query.WpCommentQuery;
import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

class CommentQueryParamMapperTest implements ParamMapperTest {

    private HttpUrl.Builder builder;

    @BeforeEach
    void beforeEach() {
        builder = Objects.requireNonNull(HttpUrl.parse("http://localhost")).newBuilder();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void fails_on_null_builder() {
        assertThatThrownBy(() -> CommentQueryParamMapper.map(null, WpCommentQuery.builder().withContext(WpContext.EDIT).build()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("builder is marked non-null but is null");
    }

    @Test
    void maps_empty() {
        CommentQueryParamMapper.map(builder, null);

        assertThatUrlContainsNoQueryParam(builder.build());
    }

    @Test
    void maps_with_after() {

        final LocalDate date = LocalDate.of(2026, 12, 1);

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withAfter(date).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        AFTER, "2026-12-01"));
    }

    @Test
    void maps_with_author() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withAuthorId(11L).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        AUTHOR, "11"));
    }

    @Test
    void maps_with_author_email() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withAuthorEmail("tom@email.com").build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        AUTHOR_EMAIL, "tom@email.com"));
    }

    @Test
    void maps_with_before() {

        final LocalDate date = LocalDate.of(2026, 12, 1);

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withBefore(date).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        BEFORE, "2026-12-01"));
    }

    @Test
    void maps_with_context() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withContext(WpContext.EDIT).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.EDIT.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue()));
    }

    @Test
    void maps_with_default_values() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue()));
    }

    @Test
    void maps_with_exclude_ids() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withExcludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        EXCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_include_ids() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withIncludeIds(Set.of(3000L, 2000L, 1000L)).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        INCLUDE, "1000,2000,3000"));
    }

    @Test
    void maps_with_many_params() {

        final LocalDate date1 = LocalDate.of(2026, 12, 1);
        final LocalDate date2 = LocalDate.of(2026, 12, 31);

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder()
                                                           .withAfter(date1)
                                                           .withBefore(date2)
                                                           .withOffset(7)
                                                           .withStatus(WpCommentStatus.APPROVED)
                                                           .withParentId(78L)
                                                           .withPostId(1500L)
                                                           .withSearch("some criteria")
                                                           .build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        AFTER, "2026-12-01",
                        BEFORE, "2026-12-31",
                        OFFSET, "7",
                        STATUS, WpCommentStatus.APPROVED.getValue(),
                        PARENT, "78",
                        POST, "1500",
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_offset() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withOffset(13).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        OFFSET, "13"));
    }

    @Test
    void maps_with_order() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withOrder(WpSortDirection.DESC).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.DESC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue()));
    }

    @Test
    void maps_with_order_by() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withAuthorId(11L).withOrderBy(WpCommentOrderFields.ID).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.ID.getValue(),
                        AUTHOR, "11"));
    }

    @Test
    void maps_with_parent() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withParentId(12345L).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        PARENT, "12345"));
    }

    @Test
    void maps_with_parent_exclude() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withExcludeParentIds(Set.of(100L, 200L, 300L)).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        PARENT_EXCLUDE, "100,200,300"));
    }

    @Test
    void maps_with_post_id() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withPostId(123456L).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        POST, "123456"));
    }

    @Test
    void maps_with_search() {
        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withSearch("some criteria").build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        SEARCH, "some criteria"));
    }

    @Test
    void maps_with_status() {

        CommentQueryParamMapper.map(builder, WpCommentQuery.builder().withStatus(WpCommentStatus.HOLD).build());

        assertThatUrlContainsExactly(builder.build(),
                Map.of(CONTEXT, WpContext.VIEW.getValue(),
                        ORDER, WpSortDirection.ASC.getValue(),
                        ORDER_BY, WpCommentOrderFields.DATE_GMT.getValue(),
                        STATUS, WpCommentStatus.HOLD.getValue()));
    }
}
