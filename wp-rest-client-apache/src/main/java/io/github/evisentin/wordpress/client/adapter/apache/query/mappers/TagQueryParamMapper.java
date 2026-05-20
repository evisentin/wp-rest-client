package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.hc.core5.net.URIBuilder;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

/**
 * Maps {@link WpTagQuery} into HTTP query parameters for tag-related WordPress endpoints.
 *
 * <p>This class translates query object fields into the corresponding
 * parameters expected by the WordPress REST API.
 *
 * <p>Null values are ignored and not added to the request.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagQueryParamMapper extends AbstractQueryParamMapper {

    /**
     * Maps the given {@link WpTagQuery} into query parameters and adds them to the provided {@link URIBuilder}.
     *
     * <p>If the query is {@code null}, no parameters are added.
     *
     * @param builder
     *         the HTTP URL builder to which query parameters are added
     * @param query
     *         the tag query object containing filter criteria
     */
    public static void map(final @NonNull URIBuilder builder, final WpTagQuery query) {

        if (query == null) return;

        addEnum(builder, CONTEXT, query.getContext());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addBoolean(builder, HIDE_EMPTY, query.getHideEmpty());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addInteger(builder, OFFSET, query.getOffset());
        addEnum(builder, ORDER, query.getOrder());
        addEnum(builder, ORDER_BY, query.getOrderBy());
        addString(builder, SEARCH, query.getSearch());
        addString(builder, SLUG, query.getSlug());
        addLong(builder, POST, query.getPostId());
    }
}
