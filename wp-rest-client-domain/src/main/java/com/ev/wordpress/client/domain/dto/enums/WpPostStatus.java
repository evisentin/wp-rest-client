package com.ev.wordpress.client.domain.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of post statuses supported by the WordPress REST API.
 *
 * <p>
 * These values define the publication state of a post, such as published, draft, scheduled, or private.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpPostStatus implements WpHasValueEnum {

    /**
     * Published post.
     */
    PUBLISH("publish"),

    /**
     * Scheduled post.
     */
    FUTURE("future"),

    /**
     * Draft post.
     */
    DRAFT("draft"),

    /**
     * Pending review post.
     */
    PENDING("pending"),

    /**
     * Private post.
     */
    PRIVATE("private"),

    /**
     * Trashed post.
     */
    TRASH("trash");

    /**
     * The API value associated with the post status.
     */
    @JsonValue
    private final String value;
}
