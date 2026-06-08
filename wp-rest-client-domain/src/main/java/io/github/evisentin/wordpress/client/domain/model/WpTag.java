package io.github.evisentin.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpTag {

    /**
     * Unique identifier of the tag.
     */
    private long id;

    /**
     * Number of times this tag is used.
     */
    private long count;

    /**
     * Human-readable description of the tag.
     */
    private String description;

    /**
     * URL link to the tag resource.
     */
    private String link;

    /**
     * Display name of the tag.
     */
    private String name;

    /**
     * URL-friendly slug of the tag.
     */
    private String slug;

    /**
     * Taxonomy type associated with this tag.
     */
    private WpTaxonomy taxonomy;
}
