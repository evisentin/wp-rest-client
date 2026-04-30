package com.ev.wordpress.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing sort directions for WordPress REST API queries.
 *
 * <p>
 * These values define whether results should be returned in ascending or descending order.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpSortDirection implements WpHasValueEnum {

    /**
     * Ascending order (lowest to highest).
     */
    ASC("asc"),

    /**
     * Descending order (highest to lowest).
     */
    DESC("desc");

    /**
     * The API value associated with the sort direction.
     */
    @JsonValue
    private final String value;
}
