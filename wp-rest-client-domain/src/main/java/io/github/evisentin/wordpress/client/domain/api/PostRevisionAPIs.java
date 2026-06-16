package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPostRevision;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostRevisionQuery;

/**
 * Defines operations for managing WordPress post revisions through the WordPress REST API.
 *
 * <p>This interface provides methods for listing and retrieving {@link WpPostRevision} resources associated with a
 * parent post.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/posts/<parent>/revisions} endpoint or
 * compatible APIs.</p>
 */
public interface PostRevisionAPIs {
    /**
     * Retrieves a specific post revision by its unique identifier.
     *
     * <p>Retrieves a single revision record belonging to the specified parent post.</p>
     *
     * @param postId
     *         the ID of the parent post
     * @param revisionId
     *         the unique identifier of the revision to retrieve
     *
     * @return the matching {@link WpPostRevision}
     */
    WpPostRevision get(long postId, long revisionId);

    /**
     * Retrieves a paginated list of revisions for a post.
     *
     * <p>The result set can be controlled and filtered using pagination and revision query parameters.</p>
     *
     * @param postId
     *         the ID of the parent post whose revisions should be listed
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param postQuery
     *         additional revision query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpPostRevision} items
     */
    WpPagedResponse<WpPostRevision> list(long postId, WpPagingQuery pageQuery, WpPostRevisionQuery postQuery);
}
