package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of page statuses supported by the WordPress REST API.
 *
 * <p>
 * These values define the publication state of a page, such as published, draft, scheduled, or private.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpPageStatus implements WpHasValueEnum {

    /**
     * Published page.
     */
    PUBLISH("publish"),

    /**
     * Scheduled page.
     */
    FUTURE("future"),

    /**
     * Draft page.
     */
    DRAFT("draft"),

    /**
     * Pending review page.
     */
    PENDING("pending"),

    /**
     * Private page.
     */
    PRIVATE("private"),

    /**
     * Trashed page.
     */
    TRASH("trash");

    /**
     * The API value associated with the page status.
     */
    @JsonValue
    private final String value;
}
