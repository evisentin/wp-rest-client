package com.ev.wordpress.client.domain.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of WordPress taxonomies supported by the client.
 *
 * <p>
 * A taxonomy defines how content is grouped in WordPress, for example by category or tag.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpTaxonomy implements WpHasValueEnum {

    /**
     * Taxonomy representing categories.
     */
    CATEGORY("category"),

    /**
     * Taxonomy representing post tags.
     */
    POST_TAG("post_tag");

    /**
     * The API value associated with the taxonomy.
     */
    @JsonValue
    private final String value;
}
