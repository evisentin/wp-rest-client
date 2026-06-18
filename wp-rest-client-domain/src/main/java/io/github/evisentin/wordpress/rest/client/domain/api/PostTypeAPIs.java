package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPostType;

import java.util.Map;

/**
 * Defines operations for retrieving WordPress post types through the WordPress REST API.
 *
 * <p>This interface provides methods for retrieving individual post type definitions as well as the complete
 * collection of registered post types available on a WordPress site.</p>
 *
 * <p>Post types describe the structure and behaviour of content resources such as posts, pages, attachments, and
 * custom post types. Returned metadata may include labels, capabilities, taxonomies, supported features, visibility
 * settings, and REST API configuration.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/types} endpoint or compatible
 * APIs.</p>
 */
public interface PostTypeAPIs {
    /**
     * Retrieves a post type definition by its unique name.
     *
     * <p>Examples include {@code post}, {@code page}, {@code attachment}, and custom post type names registered by
     * plugins or themes.</p>
     *
     * @param name
     *         the post type name; must not be {@code null}
     *
     * @return the matching {@link WpPostType}
     */
    WpPostType get(String name);

    /**
     * Retrieves all registered post types.
     *
     * <p>The returned map is keyed by post type name and contains the corresponding {@link WpPostType}
     * definitions.</p>
     *
     * @return a map of post type names to post type definitions
     */
    Map<String, WpPostType> list();
}
