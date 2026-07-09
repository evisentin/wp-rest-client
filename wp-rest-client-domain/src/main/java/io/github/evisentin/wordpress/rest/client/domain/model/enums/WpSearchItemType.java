package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of searchable resource types supported by the WordPress REST API.
 *
 * <p>These values correspond to the {@code type} query parameter accepted by the WordPress search endpoint.</p>
 *
 * <p>Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.</p>
 */
@Getter
@RequiredArgsConstructor
public enum WpSearchItemType implements WpHasValueEnum {

    /**
     * Search post resources, including custom post types.
     */
    POST("post"),

    /**
     * Search taxonomy terms.
     */
    TERM("term"),

    /**
     * Search post formats.
     */
    POST_FORMAT("post-format");

    /**
     * The API value associated with the search item type.
     */
    @JsonValue
    private final String value;
}
