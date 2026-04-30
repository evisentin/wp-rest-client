package com.ev.wordpress.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of post formats supported by the WordPress REST API.
 *
 * <p>
 * Post formats describe the presentation style of a post, such as standard content, images, quotes, or video.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpPostFormat implements WpHasValueEnum {

    /**
     * Standard post format.
     */
    STANDARD("standard"),

    /**
     * Aside post format.
     */
    ASIDE("aside"),

    /**
     * Chat post format.
     */
    CHAT("chat"),

    /**
     * Gallery post format.
     */
    GALLERY("gallery"),

    /**
     * Link post format.
     */
    LINK("link"),

    /**
     * Image post format.
     */
    IMAGE("image"),

    /**
     * Quote post format.
     */
    QUOTE("quote"),

    /**
     * Status post format.
     */
    STATUS("status"),

    /**
     * Video post format.
     */
    VIDEO("video"),

    /**
     * Audio post format.
     */
    AUDIO("audio");

    /**
     * The API value associated with the post format.
     */
    @JsonValue
    private final String value;
}
