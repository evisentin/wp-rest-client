package io.github.evisentin.wordpress.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of media item statuses supported by the WordPress REST API.
 *
 * <p>
 * Media items in WordPress are represented as {@code attachment} posts and therefore use the standard WordPress post
 * status system.
 * </p>
 *
 * <p>
 * The most common status for uploaded media is {@link #INHERIT}, which indicates that the media item inherits its
 * visibility and lifecycle from its parent post.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding REST API value stored in {@link #value}. The value is serialized to JSON
 * using {@link JsonValue}.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpMediaStatus implements WpHasValueEnum {

    /**
     * Media item inherits visibility and state from its parent post.
     *
     * <p>
     * This is the default status for uploaded media attachments.
     * </p>
     */
    INHERIT("inherit"),

    /**
     * Private media item visible only to authorized users.
     */
    PRIVATE("private"),

    /**
     * Media item moved to trash.
     */
    TRASH("trash"),

    /**
     * Published media item.
     */
    PUBLISH("publish"),

    /**
     * Draft media item.
     */
    DRAFT("draft"),

    /**
     * Automatically created draft media item.
     */
    AUTO_DRAFT("auto-draft");

    /**
     * REST API value associated with the media status.
     */
    @JsonValue
    private final String value;
}
