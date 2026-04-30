package com.ev.wordpress.client.domain.model;

import com.ev.wordpress.client.domain.model.enums.WpTaxonomy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress tag as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a tag object, including metadata
 * such as name, slug, usage count, and associated taxonomy.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to ensure
 * forward compatibility with the API.</p>
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
