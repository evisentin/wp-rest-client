package io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import okhttp3.HttpUrl;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

/**
 * Maps {@link WpCategoryQuery} into HTTP query parameters for category-related WordPress endpoints.
 *
 * <p>This class converts the fields of a query object into the corresponding
 * query parameters expected by the WordPress REST API.
 *
 * <p>Null values are ignored and not added to the request.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryQueryParamMapper extends AbstractQueryParamMapper {
    /**
     * Maps the given {@link WpCategoryQuery} into query parameters and adds them to the provided
     * {@link HttpUrl.Builder}.
     *
     * <p>If the query is {@code null}, no parameters are added.
     *
     * @param builder
     *         the HTTP URL builder to which query parameters are added
     * @param query
     *         the category query object containing filter criteria
     */
    public static void map(final @NonNull HttpUrl.Builder builder, final WpCategoryQuery query) {

        if (query == null) return;

        addEnum(builder, CONTEXT, query.getContext());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addBoolean(builder, HIDE_EMPTY, query.getHideEmpty());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addEnum(builder, ORDER, query.getOrder());
        addEnum(builder, ORDER_BY, query.getOrderBy());
        addString(builder, SEARCH, query.getSearch());
        addString(builder, SLUG, query.getSlug());
        addLong(builder, PARENT, query.getParentId());
        addLong(builder, POST, query.getPostId());
    }
}
