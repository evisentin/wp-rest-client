package io.github.evisentin.wordpress.rest.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Represents a WordPress taxonomy as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a taxonomy object, including its title, slug,
 * description, associated object types, hierarchy behaviour, and REST route information.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/taxonomies/">WordPress REST API - Taxonomies</a>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpTaxonomyInfo {

    /**
     * Title of the taxonomy.
     */
    private String name;

    /**
     * Alphanumeric identifier of the taxonomy.
     */
    private String slug;

    /**
     * Human-readable description of the taxonomy.
     */
    private String description;

    /**
     * Object types associated with this taxonomy, such as {@code post}.
     */
    private Set<String> types;

    /**
     * Whether this taxonomy supports hierarchical terms.
     */
    private boolean hierarchical;

    /**
     * REST base route for this taxonomy.
     */
    @JsonProperty("rest_base")
    private String restBase;

    /**
     * REST namespace route for this taxonomy.
     */
    @JsonProperty("rest_namespace")
    private String restNamespace;
}
