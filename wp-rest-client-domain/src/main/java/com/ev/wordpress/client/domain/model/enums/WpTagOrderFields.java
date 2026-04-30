package com.ev.wordpress.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of fields that can be used to order WordPress tag queries.
 * <p>
 * These values correspond to the supported ordering parameters in the WordPress REST API for tags.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 *     WpTagOrderFields field = WpTagOrderFields.NAME;
 *     String apiValue = field.getValue(); // "name"
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum WpTagOrderFields implements WpHasValueEnum {

    /**
     * Order by tag ID.
     */
    ID("id"),

    /**
     * Order by included IDs.
     */
    INCLUDE("include"),

    /**
     * Order by tag name.
     */
    NAME("name"),

    /**
     * Order by tag slug.
     */
    SLUG("slug"),

    /**
     * Order by included slugs.
     */
    INCLUDE_SLUGS("include_slugs"),

    /**
     * Order by term group.
     */
    TERM_GROUP("term_group"),

    /**
     * Order by tag description.
     */
    DESCRIPTION("description"),

    /**
     * Order by usage count.
     */
    COUNT("count");

    /**
     * The API value associated with the enum constant.
     * <p>
     * This value is used for JSON serialization when interacting with the WordPress REST API.
     * </p>
     */
    @JsonValue
    private final String value;
}
