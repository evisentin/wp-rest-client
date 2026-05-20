package io.github.evisentin.wordpress.client.domain.model.query;

import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTagOrderFields;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Represents a query object for retrieving WordPress tags.
 *
 * <p>This class encapsulates filtering and sorting options supported by the
 * WordPress tags endpoint.
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.
 */
@Getter
@Builder(setterPrefix = "with")
public class WpTagQuery {

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Limits results to tags matching the given search term.
     */
    private final String search;

    /**
     * Set of tag IDs to exclude from the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeIds = emptySet();

    /**
     * Set of tag IDs to include in the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> includeIds = emptySet();

    /**
     * Number of items to skip before starting to collect the result set.
     */
    private final Integer offset;

    /**
     * Sort direction of the result. Defaults to {@link WpSortDirection#ASC}.
     */
    @Builder.Default
    private final WpSortDirection order = WpSortDirection.ASC;

    /**
     * Field used to sort the result. Defaults to {@link WpTagOrderFields#NAME}.
     */
    @Builder.Default
    private final WpTagOrderFields orderBy = WpTagOrderFields.NAME;

    /**
     * Whether to hide tags that are not assigned to any posts.
     */
    private final Boolean hideEmpty;

    /**
     * Limits results to tags with the specified slug.
     */
    private final String slug;

    /**
     * Limits results to tags assigned to the specified post.
     */
    private final Long postId;
}
