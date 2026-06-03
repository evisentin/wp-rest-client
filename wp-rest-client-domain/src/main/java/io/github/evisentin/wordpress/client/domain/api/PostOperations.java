package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import lombok.NonNull;

/**
 * Defines operations for managing WordPress posts through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving, updating, listing, trashing, and deleting
 * {@link WpPost} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as pagination, filtering, retrieval contexts,
 * password-protected content, and partial querying.</p>
 *
 * <p>Implementations are expected to communicate with the
 * {@code /wp-json/wp/v2/posts} endpoint or compatible APIs.</p>
 */
public interface PostOperations {
    /**
     * Creates a new post.
     *
     * <p>Creates a post in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the post creation request; must not be {@code null}
     *
     * @return the created {@link WpPost}
     */
    WpPost createPost(@NonNull WpPostCreateUpdateRequest creationRequest);

    /**
     * Permanently deletes a post by its unique identifier.
     *
     * @param id
     *         the ID of the post to delete
     *
     * @return the post deletion response
     */
    WpPostDeletionResponse deletePost(long id);

    /**
     * Retrieves a post by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the post to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpPost}
     */
    WpPost getPost(long id, WpContext context);

    /**
     * Retrieves a post by its unique identifier using the given context and optional password.
     *
     * <p>The context determines which fields are included in the response, for example {@link WpContext#VIEW},
     * {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * <p>If the post is password protected, the provided password is used to access protected content.
     * If the password is missing or incorrect, protected fields such as content or excerpt may be omitted or flagged as
     * protected in the response.</p>
     *
     * @param id
     *         the ID of the post to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     * @param password
     *         the password for protected content; may be {@code null} when not required
     *
     * @return the matching {@link WpPost}
     */
    WpPost getPost(long id, WpContext context, String password);

    /**
     * Retrieves a paginated list of posts using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpPostQuery}, for example by filtering on search criteria,
     * include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param postQuery
     *         additional post query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpPost} items
     */
    WpPagedResponse<WpPost> listPosts(@NonNull WpPagingQuery pageQuery, WpPostQuery postQuery);

    /**
     * Moves a post to the trash by its unique identifier.
     *
     * @param id
     *         the ID of the post to trash
     *
     * @return the trashed {@link WpPost}
     */
    WpPost trashPost(long id);

    /**
     * Updates an existing post.
     *
     * <p>Updates a post in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the post to update
     * @param updateRequest
     *         the post update request; must not be {@code null}
     *
     * @return the updated {@link WpPost}
     */
    WpPost updatePost(long id, @NonNull WpPostCreateUpdateRequest updateRequest);
}
