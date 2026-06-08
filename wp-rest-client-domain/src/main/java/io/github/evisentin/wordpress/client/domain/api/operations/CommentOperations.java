package io.github.evisentin.wordpress.client.domain.api.operations;

import io.github.evisentin.wordpress.client.domain.model.WpComment;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCommentQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCommentCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCommentDeletionResponse;
import lombok.NonNull;

/**
 * Defines operations for managing WordPress comments through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving, updating, listing, trashing, and deleting
 * {@link io.github.evisentin.wordpress.client.domain.model.WpComment} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as pagination, filtering, retrieval contexts,
 * password-protected content, and partial querying.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/comments} endpoint or compatible
 * APIs.</p>
 */
public interface CommentOperations {

    /**
     * Creates a new comment.
     *
     * <p>Creates a comment in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the comment creation request; must not be {@code null}
     *
     * @return the created {@link WpComment}
     */
    WpComment createComment(@NonNull WpCommentCreateUpdateRequest creationRequest);

    /**
     * Permanently deletes a comment by its unique identifier.
     *
     * @param id
     *         the ID of the comment to delete
     *
     * @return the comment deletion response
     */
    WpCommentDeletionResponse deleteComment(long id);

    /**
     * Retrieves a comment by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the comment to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpComment}
     */
    WpComment getComment(long id, WpContext context);

    /**
     * Retrieves a comment by its unique identifier using the given context and optional password.
     *
     * <p>The context determines which fields are included in the response, for example {@link WpContext#VIEW},
     * {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * <p>If the comment is password protected, the provided password is used to access protected content.
     * If the password is missing or incorrect, protected fields such as content may be omitted or flagged as protected
     * in the response.</p>
     *
     * @param id
     *         the ID of the comment to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     * @param password
     *         the password for protected content; may be {@code null} when not required
     *
     * @return the matching {@link WpComment}
     */
    WpComment getComment(long id, WpContext context, String password);

    /**
     * Retrieves a paginated list of comments using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpCommentQuery}, for example by filtering on search criteria,
     * include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param commentQuery
     *         additional comment query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpComment} items
     */
    WpPagedResponse<WpComment> listComments(@NonNull WpPagingQuery pageQuery, WpCommentQuery commentQuery);

    /**
     * Moves a comment to the trash by its unique identifier.
     *
     * @param id
     *         the ID of the comment to trash
     *
     * @return the trashed {@link WpComment}
     */
    WpComment trashComment(long id);

    /**
     * Updates an existing comment.
     *
     * <p>Updates a comment in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the comment to update
     * @param updateRequest
     *         the post update request; must not be {@code null}
     *
     * @return the updated {@link WpComment}
     */
    WpComment updateComment(long id, @NonNull WpCommentCreateUpdateRequest updateRequest);
}
