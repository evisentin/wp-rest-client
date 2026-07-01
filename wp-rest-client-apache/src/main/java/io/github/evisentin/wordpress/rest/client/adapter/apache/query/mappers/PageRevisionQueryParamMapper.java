package io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageRevisionQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.hc.core5.net.URIBuilder;

import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageRevisionQueryParamMapper extends AbstractQueryParamMapper {

    public static void map(final @NonNull URIBuilder builder, final WpPageRevisionQuery query) {

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
