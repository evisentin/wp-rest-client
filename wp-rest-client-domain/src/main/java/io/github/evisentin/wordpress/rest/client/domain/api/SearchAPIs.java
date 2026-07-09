package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpSearchResult;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpSearchQuery;

/**
 * Defines operations for searching resources through the WordPress REST API.
 *
 * <p>This interface provides access to the WordPress search endpoint, allowing clients to search across supported
 * resource types such as posts, terms, and post formats.</p>
 *
 * <p>Search results can be refined using pagination and filtering criteria provided by {@link WpSearchQuery}.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/search} endpoint or compatible
 * APIs.</p>
 */
public interface SearchAPIs {

    /**
     * Performs a search using the supplied pagination and filtering criteria.
     *
     * <p>The query supports filtering by search term, resource type, included or excluded identifiers, and
     * subtype.</p>
     *
     * @param paginationQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param query
     *         additional search criteria; may be {@code null} when only pagination is required
     *
     * @return a paginated response containing matching {@link WpSearchResult} items
     */
    WpPagedResponse<WpSearchResult> search(WpPaginationQuery paginationQuery, WpSearchQuery query);
}
