package io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.rest.client.domain.model.query.WpSearchQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import okhttp3.HttpUrl;

import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.*;

/**
 * Maps {@link WpSearchQuery} into HTTP query parameters for search-related WordPress endpoints.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchQueryParamMapper extends AbstractQueryParamMapper {
    /**
     * Maps the given {@link WpSearchQuery} into query parameters and adds them to the provided
     * {@link HttpUrl.Builder}.
     *
     * <p>If the query is {@code null}, no parameters are added.
     *
     * @param builder
     *         the HTTP URL builder to which query parameters are added
     * @param query
     *         the post query object containing filter criteria
     */
    public static void map(final @NonNull HttpUrl.Builder builder, final WpSearchQuery query) {

        if (query == null) return;

        addEnum(builder, CONTEXT, query.getContext());
        addString(builder, SEARCH, query.getSearch());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addEnum(builder, TYPE, query.getType());
        addEnum(builder, SUBTYPE, query.getSubType());
    }
}
