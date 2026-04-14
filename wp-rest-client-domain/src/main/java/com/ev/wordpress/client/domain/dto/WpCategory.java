package com.ev.wordpress.client.domain.dto;

import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress category as returned by the WordPress REST API.
 *
 * <p>This model maps the JSON structure of a category object, including
 * metadata such as its name, slug, description, usage count, taxonomy, parent category, and custom meta fields.</p>
 *
 * <p>Unknown JSON properties are ignored during deserialization to provide
 * forward compatibility with newer WordPress API responses.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpCategory {

    /**
     * Unique identifier of the category.
     */
    private long id;

    /**
     * Number of posts assigned to this category.
     */
    private long count;

    /**
     * Human-readable description of the category.
     */
    private String description;

    /**
     * URL of the category resource.
     */
    private String link;

    /**
     * Display name of the category.
     */
    private String name;

    /**
     * URL-friendly slug of the category.
     */
    private String slug;

    /**
     * Taxonomy type associated with this category.
     */
    private WpTaxonomy taxonomy;

    /**
     * Identifier of the parent category, or {@code null} if this category has no parent.
     */
    @JsonProperty("parent")
    private Long parentId;
}
