package com.ev.wordpress.client.domain.dto.query;

import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpPostStatus;
import com.ev.wordpress.client.domain.dto.enums.WpSortDirection;
import com.ev.wordpress.client.domain.dto.enums.WpTagOrderFields;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Represents a query object for retrieving WordPress posts.
 *
 * <p>This class encapsulates filtering and sorting options supported by the
 * WordPress posts endpoint.
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.
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
    @Builder.Default
    private final Set<Long> excludeAuthorIds = emptySet();

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
     * Field used to sort the result. Defaults to {@link WpTagOrderFields#NAME}.
     */
    @Builder.Default
    private final WpTagOrderFields orderBy = WpTagOrderFields.NAME;

    /**
     * Set of post slugs to include in the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<String> slugs = emptySet();

    /**
     * Set of post statuses to include in the result. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<WpPostStatus> statuses = emptySet();

    // tax_relation
    // search_columns

    /**
     * Set of category IDs that posts must belong to. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> includeCategories = emptySet();

    /**
     * Set of category IDs that posts must not belong to. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeCategories = emptySet();

    /**
     * Set of tag IDs that posts must have. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> includeTags = emptySet();

    /**
     * Set of tag IDs that posts must not have. Defaults to an empty set.
     */
    @Builder.Default
    private final Set<Long> excludeTags = emptySet();

    /**
     * Whether to limit results to sticky posts.
     */
    private Boolean sticky;
}
