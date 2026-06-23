package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPage;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPageCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;

import java.util.Map;

/**
 * Defines operations for managing WordPress pages through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving, updating, listing, trashing, and deleting
 * {@link WpPage} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as pagination, filtering, retrieval contexts,
 * password-protected content, and partial querying.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/pages} endpoint or compatible
 * APIs.</p>
 */
public interface PageAPIs {
    /**
     * Creates a new page.
     *
     * <p>Creates a page in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the page creation request; must not be {@code null}
     *
     * @return the created {@link WpPage}
     */
    WpPage create(WpPageCreateUpdateRequest creationRequest);

    /**
     * /**
     * <p>
     * Creates a new page.
     *
     * @param creationRequest
     *         the page creation request; must not be {@code null}
     * @param extraQueryParams
     *         additional query parameters to include in the request.
     *
     * @return the created {@link WpPage}
     */
    WpPage create(WpPageCreateUpdateRequest creationRequest, Map<String, String> extraQueryParams);

    /**
     * Permanently deletes a page by its unique identifier.
     *
     * @param id
     *         the ID of the page to delete
     *
     * @return the page deletion response
     */
    WpPageDeletionResponse delete(long id);

    /**
     * Retrieves a page by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the page to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpPage}
     */
    WpPage get(long id, WpContext context);

    /**
     * Retrieves a page by its unique identifier using the given context and optional password.
     *
     * <p>The context determines which fields are included in the response, for example {@link WpContext#VIEW},
     * {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * <p>If the page is password protected, the provided password is used to access protected content.
     * If the password is missing or incorrect, protected fields such as content or excerpt may be omitted or flagged as
     * protected in the response.</p>
     *
     * @param id
     *         the ID of the page to retrieve
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     * @param password
     *         the password for protected content; may be {@code null} when not required
     *
     * @return the matching {@link WpPage}
     */
    WpPage get(long id, WpContext context, String password);

    /**
     * Retrieves a paginated list of pages using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpPageQuery}, for example by filtering on search criteria,
     * include or exclude lists, or sort order.</p>
     *
     * @param paginationQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param pageQuery
     *         additional page query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpPage} items
     */
    WpPagedResponse<WpPage> list(WpPaginationQuery paginationQuery, WpPageQuery pageQuery);

    /**
     * Moves a page to the trash by its unique identifier.
     *
     * @param id
     *         the ID of the page to trash
     *
     * @return the trashed {@link WpPage}
     */
    WpPage trash(long id);

    /**
     * Updates an existing page.
     *
     * <p>Updates a page in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the page to update
     * @param updateRequest
     *         the page update request; must not be {@code null}
     *
     * @return the updated {@link WpPage}
     */
    WpPage update(long id, WpPageCreateUpdateRequest updateRequest);
}
