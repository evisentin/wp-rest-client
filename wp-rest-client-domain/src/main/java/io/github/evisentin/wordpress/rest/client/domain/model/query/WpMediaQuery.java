package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpMediaType;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpMediaOrderFields;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.time.LocalDate;
import java.util.Set;

/**
 * Query parameters for listing WordPress media items.
 *
 * <p>This model maps the filtering and sorting parameters supported by the
 * {@code GET /wp-json/wp/v2/media} endpoint.</p>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpMediaQuery {

    /**
     * Scope under which the request is made. Defaults to {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Set of author IDs to exclude from the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeAuthorIds;

    /**
     * Number of items to skip before starting to collect the result set.
     */
    private final Integer offset;

    /**
     * Field used to sort the result. Defaults to {@link WpMediaOrderFields#ID}.
     */
    @Builder.Default
    private final WpMediaOrderFields orderBy = WpMediaOrderFields.ID;

    /**
     * Limits results to media items matching the given search term.
     */
    private final String search;

    /**
     * Limits results to media items published before the specified date.
     */
    private final LocalDate before;

    /**
     * Limits results to media items published after the specified date.
     */
    private final LocalDate after;

    /**
     * Limits results to media items modified before the specified date.
     */
    private final LocalDate modifiedBefore;

    /**
     * Limits results to media items modified after the specified date.
     */
    private final LocalDate modifiedAfter;

    /**
     * Limits results to media items uploaded by the specified author.
     */
    private final Long authorId;

    /**
     * Set of media item IDs to exclude from the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeIds;

    /**
     * Set of media item IDs to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeIds;

    /**
     * Sort direction of the result. Defaults to {@link WpSortDirection#ASC}.
     */
    @Builder.Default
    private final WpSortDirection order = WpSortDirection.ASC;

    /**
     * Set of media item slugs to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<String> slugs;

    /**
     * Set of media item statuses to include in the result. Defaults to an empty set.
     */
    @Singular
    private final Set<WpMediaStatus> statuses;

    /**
     * Set of parent post IDs that media items must be attached to. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeParents;

    /**
     * Set of parent post IDs that media items must not be attached to. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeParents;

    /**
     * Limits results to attachments of the specified media type.
     */
    private final WpMediaType mediaType;

    /**
     * Limits results to attachments of the specified MIME type.
     */
    private final String mimeType;
}
