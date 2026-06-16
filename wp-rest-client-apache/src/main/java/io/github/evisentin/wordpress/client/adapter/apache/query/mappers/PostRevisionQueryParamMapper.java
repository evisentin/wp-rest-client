package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.query.WpPostRevisionQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.hc.core5.net.URIBuilder;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRevisionQueryParamMapper extends AbstractQueryParamMapper {

    public static void map(final @NonNull URIBuilder builder, final WpPostRevisionQuery query) {

        if (query == null) return;

        addLong(builder, PARENT, query.getParentId());
        addEnum(builder, CONTEXT, query.getContext());
        addString(builder, SEARCH, query.getSearch());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addInteger(builder, OFFSET, query.getOffset());
        addEnum(builder, ORDER, query.getOrder());
        addEnum(builder, ORDER_BY, query.getOrderBy());
    }
}
