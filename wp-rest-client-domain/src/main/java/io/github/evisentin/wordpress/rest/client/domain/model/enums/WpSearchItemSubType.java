package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of searchable resource subtypes supported by the WordPress REST API.
 *
 * <p>These values correspond to the {@code subtype} query parameter accepted by the WordPress search endpoint.</p>
 *
 * <p>Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.</p>
 */
@Getter
@RequiredArgsConstructor
public enum WpSearchItemSubType implements WpHasValueEnum {

    CATEGORY("category"),
    PAGE("page"),
    POST("post"),
    POST_TAG("post_tag");

    /**
     * The API value associated with the search item type.
     */
    @JsonValue
    private final String value;
}
