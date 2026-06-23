package io.github.evisentin.wordpress.rest.client.domain.model.query;

import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpCommentStatus;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpSortDirection;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.order.WpCommentOrderFields;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.time.LocalDate;
import java.util.Set;

/**
 * Represents a query object for retrieving WordPress comments.
 *
 * <p>This model encapsulates the filtering, pagination, and sorting options supported by the WordPress Comments REST
 * API endpoint.</p>
 *
 * <p>Instances are immutable and should be created using the {@link Builder}.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/comments/#arguments">WordPress REST API - Comment
 * Query Arguments</a>
 */
@Getter
@Builder(setterPrefix = "with")
public class WpCommentQuery {

    /**
     * Scope under which the request is made. Controls the level of detail included in the response. Defaults to
     * {@link WpContext#VIEW}.
     */
    @Builder.Default
    private final WpContext context = WpContext.VIEW;

    /**
     * Limits results to comments matching the specified search term.
     */
    private final String search;

    /**
     * Limits results to comments published before the specified date.
     */
    private final LocalDate before;

    /**
     * Limits results to comments published after the specified date.
     */
    private final LocalDate after;

    /**
     * Limits results to comments authored by the specified user.
     */
    private final Long authorId;

    /**
     * Excludes comments authored by any of the specified users. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> authorExcludeIds;

    /**
     * Limits results to comments authored using the specified email address.
     */
    private final String authorEmail;

    /**
     * Excludes comments with the specified identifiers. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeIds;

    /**
     * Limits results to comments with the specified identifiers. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> includeIds;

    /**
     * Number of comments to skip before collecting the result set.
     */
    private final Integer offset;

    /**
     * Sort direction of the result set. Defaults to {@link WpSortDirection#ASC}.
     */
    @Builder.Default
    private final WpSortDirection order = WpSortDirection.ASC;

    /**
     * Field used to sort the result. Defaults to {@link WpCommentOrderFields#DATE_GMT}.
     */
    @Builder.Default
    private final WpCommentOrderFields orderBy = WpCommentOrderFields.DATE_GMT;

    /**
     * Excludes comments whose parent identifier matches any of the specified values. Defaults to an empty set.
     */
    @Singular
    private final Set<Long> excludeParentIds;

    /**
     * Limits results to comments of the specified type. Typical values include {@code comment}, {@code pingback}, and
     * {@code trackback}.
     */
    private final String type;

    /**
     * Limits results to comments associated with password-protected content matching the specified password.
     */
    private final String password;

    /**
     * Limits results to replies of the specified parent comment.
     */
    private final Long parentId;

    /**
     * Limits results to comments belonging to the specified post.
     */
    private final Long postId;

    /**
     * Limits results to comments having the specified moderation status.
     */
    private final WpCommentStatus status;
}
