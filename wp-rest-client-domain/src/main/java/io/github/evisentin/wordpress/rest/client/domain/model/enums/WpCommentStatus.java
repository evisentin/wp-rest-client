package io.github.evisentin.wordpress.rest.client.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of comment statuses supported by the WordPress REST API.
 *
 * <p>
 * These values represent the moderation state of a comment and determine whether it is publicly visible, awaiting
 * review, marked as spam, or removed from normal display.
 * </p>
 *
 * <p>
 * Each enum constant maps to the corresponding REST API value stored in {@link #value}, which is used during JSON
 * serialization via the {@link JsonValue} annotation.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum WpCommentStatus implements WpHasValueEnum {

    /**
     * Approved comment that is publicly visible.
     */
    APPROVED("approved"),

    /**
     * Comment awaiting moderation and not yet visible publicly.
     */
    HOLD("hold"),

    /**
     * Comment marked as spam.
     */
    SPAM("spam"),

    /**
     * Comment moved to trash.
     */
    TRASH("trash");

    /**
     * The REST API value associated with the comment status.
     */
    @JsonValue
    private final String value;
}
