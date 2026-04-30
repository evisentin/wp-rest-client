package com.ev.wordpress.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of context values used in WordPress REST API requests.
 *
 * <p>
 * The context parameter controls which fields are included in the API response depending on the intended use case
 * (e.g., public view, embedding, or editing).
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpContext implements WpHasValueEnum {

    /**
     * Context for editing resources, typically requiring authentication.
     */
    EDIT("edit"),

    /**
     * Context for embedding resources in other views.
     */
    EMBED("embed"),

    /**
     * Default context for public-facing views.
     */
    VIEW("view");

    /**
     * The API value associated with the context.
     */
    @JsonValue
    private final String value;
}
