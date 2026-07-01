package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing open or closed states used by the WordPress REST API.
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpOpenClosed implements WpHasValueEnum {

    /**
     * Open state.
     */
    OPEN("open"),

    /**
     * Closed state.
     */
    CLOSED("closed");

    /**
     * The API value associated with the state.
     */
    @JsonValue
    private final String value;
}
