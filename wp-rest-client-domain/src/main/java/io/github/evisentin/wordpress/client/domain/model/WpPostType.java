package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Represents a WordPress tag as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a tag object, including metadata such as its identifier, name, slug,
 * description, usage count, taxonomy, and resource link.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/tags/">WordPress REST API - Tags</a>
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpPostType {

    /**
     * Human-readable description of the post type.
     */
    private String description;

    /**
     * Display name of the post type.
     */
    private String name;

    /**
     * Unique post type identifier (e.g. {@code post}, {@code page}, {@code attachment}).
     */
    private String slug;

    /**
     * Dashicon identifier associated with the post type.
     */
    private String icon;

    /**
     * Base path used by the REST API for this post type.
     */
    @JsonProperty("rest_base")
    private String restBase;

    /**
     * REST API namespace used by this post type.
     */
    @JsonProperty("rest_namespace")
    private String restNamespace;

    /**
     * Indicates whether the post type supports a parent-child hierarchy.
     */
    private boolean hierarchical;

    /**
     * Indicates whether the post type exposes an archive endpoint.
     */
    @JsonProperty("has_archive")
    private boolean hasArchive;

    /**
     * Taxonomies associated with this post type.
     */
    private Set<WpTaxonomy> taxonomies;
}
