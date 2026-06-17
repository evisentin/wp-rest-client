package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPostRevisionOrderFields;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Represents a query object for retrieving WordPress post revisions.
 *
 * <p>This class encapsulates filtering and sorting options supported by the
 * WordPress post revisions endpoint.</p>
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.</p>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpPostRevisionQuery {

    /**
     * The ID for the parent of the revision.
     */
    private final Long parentId;

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Limits results to revisions matching the given search term.
     */
    private final String search;

    /**
     * Set of post IDs to exclude from the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeIds = emptySet();

    /**
     * Set of post IDs to include in the result. Defaults to an empty set.
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
     * Field used to sort the result. Defaults to {@link WpPostRevisionOrderFields#DATE}.
     */
    @Builder.Default
    private final WpPostRevisionOrderFields orderBy = WpPostRevisionOrderFields.DATE;
}
