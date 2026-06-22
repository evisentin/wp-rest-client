package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPageStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPageOrderFields;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Represents a query object for retrieving WordPress pages.
 *
 * <p>This class encapsulates filtering and sorting options supported by the
 * WordPress pages endpoint.</p>
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.</p>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpPageQuery {

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Limits results to pages matching the given search term.
     */
    private final String search;

    /**
     * Limits results to pages published before the specified date.
     */
    private final LocalDate before;

    /**
     * Limits results to pages published after the specified date.
     */
    private final LocalDate after;

    /**
     * Limits results to pages modified before the specified date.
     */
    private final LocalDate modifiedBefore;

    /**
     * Limits results to pages modified after the specified date.
     */
    private final LocalDate modifiedAfter;

    /**
     * Limits results to pages written by the specified author.
     */
    private final Long authorId;

    /**
     * Set of author IDs to exclude from the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeAuthorIds = emptySet();

    /**
     * Set of page IDs to exclude from the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeIds = emptySet();

    /**
     * Set of page IDs to include in the result. Defaults to an empty set.
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
     * Field used to sort the result. Defaults to {@link WpPageOrderFields#DATE}.
     */
    @Builder.Default
    private final WpPageOrderFields orderBy = WpPageOrderFields.DATE;

    /**
     * Set of page slugs to include in the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<String> slugs = emptySet();

    private final Long parent;
    private final Long menuOrder;

    @Builder.Default
    private final Set<Long> excludeParentIds = emptySet();

    /**
     * Set of page statuses to include in the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<WpPageStatus> statuses = emptySet();
}
