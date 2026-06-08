package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress post type as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a post type object, including metadata such as its name, slug, description,
 * icon, REST API configuration, hierarchy support, archive support, and associated taxonomies.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure forward compatibility with the API.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/post-types/">WordPress REST API - Post Types</a>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpStatus {

    /**
     * Human-readable display name of the status.
     *
     * <p>Examples: {@code Published}, {@code Draft}, {@code Pending Review}.
     */
    private String name;

    /**
     * Indicates whether content with this status is publicly visible.
     *
     * <p>Maps to the WordPress {@code public} property.
     */
    @JsonProperty("public")
    private boolean statusPublic;

    /**
     * Indicates whether content with this status can be queried through publicly accessible queries.
     */
    private boolean queryable;

    /**
     * Indicates whether posts with this status are considered to have a floating publication date.
     *
     * <p>Maps to the WordPress {@code date_floating} property.
     * Drafts typically have a floating date until they are published.
     */
    @JsonProperty("date_floating")
    private boolean dateFloating;

    /**
     * URL-friendly identifier of the status.
     *
     * <p>Examples: {@code publish}, {@code draft}, {@code pending},
     * {@code future}, {@code private}.
     */
    private String slug;
}
