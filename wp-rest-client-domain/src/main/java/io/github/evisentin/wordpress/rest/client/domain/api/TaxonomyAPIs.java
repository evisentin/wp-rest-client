package io.github.evisentin.wordpress.rest.client.domain.api;

import io.github.evisentin.wordpress.rest.client.domain.model.WpTaxonomyInfo;

import java.util.Map;

/**
 * Defines operations for retrieving WordPress taxonomies through the WordPress REST API.
 *
 * <p>This interface provides methods for retrieving a single {@link WpTaxonomyInfo} resource and listing available
 * taxonomy resources.</p>
 *
 * <p>Implementations are expected to communicate with the {@code /wp-json/wp/v2/taxonomies} endpoint or compatible
 * APIs.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/taxonomies/">WordPress REST API - Taxonomies</a>
 */
public interface TaxonomyAPIs {

    /**
     * Retrieves a taxonomy by its alphanumeric identifier.
     *
     * <p>The identifier corresponds to the taxonomy slug, for example {@code category} or {@code post_tag}.</p>
     *
     * @param name
     *         the taxonomy identifier; must not be {@code null}
     *
     * @return the matching {@link WpTaxonomyInfo}
     */
    WpTaxonomyInfo get(String name);

    /**
     * Retrieves the list of available taxonomies.
     *
     * @return the available {@link WpTaxonomyInfo} resources
     */
    Map<String, WpTaxonomyInfo> list();
}
