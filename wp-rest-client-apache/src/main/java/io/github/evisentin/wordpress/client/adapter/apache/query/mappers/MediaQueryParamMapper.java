package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.query.WpMediaQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.hc.core5.net.URIBuilder;

import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.*;

/**
 * Maps {@link WpMediaQuery} into HTTP query parameters for post-related WordPress endpoints.
 *
 * <p>This mapper supports a wide range of filtering options such as date ranges,
 * authors, categories, tags, and post status.
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MediaQueryParamMapper extends AbstractQueryParamMapper {
    /**
     * Maps the given {@link WpMediaQuery} into query parameters and adds them to the provided {@link URIBuilder}.
     *
     * <p>If the query is {@code null}, no parameters are added.
     *
     * @param builder
     *         the HTTP URL builder to which query parameters are added
     * @param query
     *         the post query object containing filter criteria
     */
    public static void map(final @NonNull URIBuilder builder, final WpMediaQuery query) {

        if (query == null) return;

        addEnum(builder, CONTEXT, query.getContext());
        addString(builder, SEARCH, query.getSearch());
        addLocalDate(builder, BEFORE, query.getBefore());
        addLocalDate(builder, AFTER, query.getAfter());
        addLocalDate(builder, MODIFIED_BEFORE, query.getModifiedBefore());
        addLocalDate(builder, MODIFIED_AFTER, query.getModifiedAfter());
        addLong(builder, AUTHOR, query.getAuthorId());
        addSetOfLong(builder, AUTHOR_EXCLUDE, query.getExcludeAuthorIds());
        addSetOfLong(builder, EXCLUDE, query.getExcludeIds());
        addSetOfLong(builder, INCLUDE, query.getIncludeIds());
        addInteger(builder, OFFSET, query.getOffset());
        addEnum(builder, ORDER, query.getOrder());
        addEnum(builder, ORDER_BY, query.getOrderBy());
        addSetOfStrings(builder, SLUG, query.getSlugs());
        addSetOfEnums(builder, STATUS, query.getStatuses());
        addSetOfLong(builder, PARENT, query.getIncludeParents());
        addSetOfLong(builder, PARENT_EXCLUDE, query.getExcludeParents());
        addEnum(builder, MEDIA_TYPE, query.getMediaType());
        addString(builder, MIME_TYPE, query.getMimeType());
    }
}
