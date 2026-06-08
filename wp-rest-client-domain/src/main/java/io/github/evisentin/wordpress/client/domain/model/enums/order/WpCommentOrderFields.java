package io.github.evisentin.wordpress.client.domain.model.enums.order;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.evisentin.wordpress.client.domain.model.enums.WpHasValueEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines the fields that can be used to sort WordPress comment query results.
 *
 * <p>These values map to the {@code orderby} argument supported by the WordPress Comments REST API endpoint.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/comments/#arguments">WordPress REST API - Comments
 * Arguments</a>
 */
@Getter
@RequiredArgsConstructor
public enum WpCommentOrderFields implements WpHasValueEnum {

    /**
     * Order by the comment publication date in the site's local timezone.
     */
    DATE("date"),

    /**
     * Order by the comment publication date in GMT (UTC).
     */
    DATE_GMT("date_gmt"),

    /**
     * Order by the comment identifier.
     */
    ID("id"),

    /**
     * Preserve the order of the identifiers specified in the include parameter.
     */
    INCLUDE("include"),

    /**
     * Order by the associated post identifier.
     */
    POST("post"),

    /**
     * Order by the parent comment identifier.
     */
    PARENT("parent"),

    /**
     * Order by the comment type.
     */
    TYPE("type");

    /**
     * The API value associated with the enum constant.
     * <p>
     * This value is used for JSON serialization when interacting with the WordPress REST API.
     * </p>
     */
    @JsonValue
    private final String value;
}
