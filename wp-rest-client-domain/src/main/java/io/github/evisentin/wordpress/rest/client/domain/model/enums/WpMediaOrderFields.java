package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of fields that can be used to order WordPress media queries.
 * <p>
 * These values correspond to the supported ordering parameters in the WordPress REST API for media.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 *
 */
@Getter
@RequiredArgsConstructor
public enum WpMediaOrderFields implements WpHasValueEnum {

    AUTHOR("author"),
    DATE("date"),
    ID("id"),
    INCLUDE("include"),
    INCLUDE_SLUGS("include_slugs"),
    MODIFIED("modified"),
    PARENT("parent"),
    RELEVANCE("relevance"),
    SLUG("slug"),
    TITLE("title");

    /**
     * The API value associated with the enum constant.
     * <p>
     * This value is used for JSON serialization when interacting with the WordPress REST API.
     * </p>
     */
    @JsonValue
    private final String value;
}
