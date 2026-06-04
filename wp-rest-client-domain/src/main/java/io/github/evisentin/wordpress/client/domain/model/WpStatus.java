package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress post status as exposed by the {@code /wp/v2/statuses} REST endpoint.
 *
 * <p>A post status determines the publication state and visibility of a post, page, or other content type. Typical
 * statuses include {@code publish}, {@code draft}, {@code pending}, {@code future}, and {@code private}.
 *
 * <p>Instances of this class are usually obtained by invoking the WordPress REST API {@code /wp/v2/statuses} endpoint.
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/post-statuses/"> WordPress REST API - Post Statuses
 * </a>
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
