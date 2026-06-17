package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpStatus;

import java.util.Map;

/**
 * Defines operations for retrieving WordPress post statuses through the WordPress REST API.
 *
 * <p>This interface provides methods for retrieving individual post status definitions as well as the complete
 * collection of registered post statuses available on a WordPress site.</p>
 *
 * <p>Post statuses describe the publication state and visibility of content resources, including statuses such as
 * {@code publish}, {@code draft}, {@code pending}, {@code future}, and {@code private}.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/statuses} endpoint or compatible
 * APIs.</p>
 */
public interface PostStatusAPIs {

    /**
     * Retrieves a post status definition by its unique slug.
     *
     * <p>Examples include {@code publish}, {@code draft}, {@code pending}, {@code future}, and {@code private}.</p>
     *
     * @param name
     *         the status slug; must not be {@code null}
     *
     * @return the matching {@link WpStatus}
     */
    WpStatus get(String name);

    /**
     * Retrieves all registered post statuses.
     *
     * <p>The returned map is keyed by status slug and contains the corresponding {@link WpStatus} definitions.</p>
     *
     * @return a map of status slugs to status definitions
     */
    Map<String, WpStatus> list();
}
