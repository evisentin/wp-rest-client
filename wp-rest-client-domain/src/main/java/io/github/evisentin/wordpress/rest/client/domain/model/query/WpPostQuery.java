package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpPostStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpPostOrderFields;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a query object for retrieving WordPress posts.
 *
 * <p>This class encapsulates filtering and sorting options supported by the
 * WordPress posts endpoint.</p>
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.</p>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpPostQuery {

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Limits results to posts matching the given search term.
     */
    private final String search;

    /**
     * Limits results to posts published before the specified date.
     */
    private final LocalDate before;

    /**
     * Limits results to posts published after the specified date.
     */
    private final LocalDate after;

    /**
     * Limits results to posts modified before the specified date.
     */
    private final LocalDate modifiedBefore;

    /**
     * Limits results to posts modified after the specified date.
     */
    private final LocalDate modifiedAfter;

    /**
     * Limits results to posts written by the specified author.
     */
    private final Long authorId;

    /**
     * Set of author IDs to exclude from the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeAuthorIds;

    /**
     * Set of post IDs to exclude from the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeIds;

    /**
     * Set of post IDs to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeIds;
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
     * Field used to sort the result. Defaults to {@link WpPostOrderFields#DATE}.
     */
    @Builder.Default
    private final WpPostOrderFields orderBy = WpPostOrderFields.DATE;

    /**
     * Set of post slugs to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<String> slugs;

    /**
     * Set of post statuses to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<WpPostStatus> statuses;

    // tax_relation
    // search_columns

    /**
     * Set of category IDs that posts must belong to. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeCategories;

    /**
     * Set of category IDs that posts must not belong to. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeCategories;

    /**
     * Set of tag IDs that posts must have. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeTags;

    /**
     * Set of tag IDs that posts must not have. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeTags;

    /**
     * Whether to limit results to sticky posts.
     */
    private Boolean sticky;
}
