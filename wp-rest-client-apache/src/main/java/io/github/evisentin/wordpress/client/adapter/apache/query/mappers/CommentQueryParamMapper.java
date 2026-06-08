package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.query.WpCommentQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.hc.core5.net.URIBuilder;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentQueryParamMapper extends AbstractQueryParamMapper {

    public static void map(final @NonNull URIBuilder builder, final WpCommentQuery query) {

        if (query == null) return;

        addLocalDate(builder, AFTER, query.getAfter());
        addLong(builder, AUTHOR, query.getAuthorId());
        addString(builder, AUTHOR_EMAIL, query.getAuthorEmail());
        addLocalDate(builder, BEFORE, query.getBefore());
        addEnum(builder, CONTEXT, query.getContext());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addInteger(builder, OFFSET, query.getOffset());
        addEnum(builder, ORDER, query.getOrder());
        addEnum(builder, ORDER_BY, query.getOrderBy()); //OK
        addSetOfLong(builder, PARENT_EXCLUDE, query.getExcludeParentIds());
        addString(builder, PASSWORD, query.getPassword());
        addLong(builder, PARENT, query.getParentId());
        addLong(builder, POST, query.getPostId());
        addString(builder, SEARCH, query.getSearch());
        addEnum(builder, STATUS, query.getStatus());
        addString(builder, TYPE, query.getType());
    }
}
