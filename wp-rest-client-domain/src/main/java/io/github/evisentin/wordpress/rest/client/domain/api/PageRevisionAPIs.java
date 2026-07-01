package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPageRevision;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageRevisionQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;

/**
 * Defines operations for managing WordPress post revisions through the WordPress REST API.
 *
 * <p>This interface provides methods for listing and retrieving {@link WpPageRevision} resources associated with a
 * parent post.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/posts/<parent>/revisions} endpoint or
 * compatible APIs.</p>
 */
public interface PageRevisionAPIs {
    /**
     * Retrieves a specific post revision by its unique identifier.
     *
     * <p>Retrieves a single revision record belonging to the specified parent post.</p>
     *
     * @param pageId
     *         the ID of the parent page
     * @param revisionId
     *         the unique identifier of the revision to retrieve
     *
     * @return the matching {@link WpPageRevision}
     */
    WpPageRevision get(long pageId, long revisionId);

    /**
     * Retrieves a paginated list of revisions for a post.
     *
     * <p>The result set can be controlled and filtered using pagination and revision query parameters.</p>
     *
     * @param pageId
     *         the ID of the parent page whose revisions should be listed
     * @param paginationQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param query
     *         additional revision query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpPageRevision} items
     */
    WpPagedResponse<WpPageRevision> list(long pageId, WpPaginationQuery paginationQuery, WpPageRevisionQuery query);
}
