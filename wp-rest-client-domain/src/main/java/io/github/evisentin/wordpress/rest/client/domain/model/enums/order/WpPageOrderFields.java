package io.github.evisentin.wordpress.rest.client.domain.model.enums.order;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpHasValueEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines the fields that can be used to sort WordPress page query results.
 *
 * <p>These values map to the {@code orderby} argument supported by the WordPress Pages REST API endpoint.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/pages/#arguments">WordPress REST API - Pages
 * Arguments</a>
 */
@Getter
@RequiredArgsConstructor
public enum WpPageOrderFields implements WpHasValueEnum {

    /**
     * Order by the author identifier.
     */
    AUTHOR("author"),

    /**
     * Order by the publication date.
     */
    DATE("date"),

    /**
     * Order by the page identifier.
     */
    ID("id"),

    /**
     * Preserve the order of the identifiers specified in the include parameter.
     */
    INCLUDE("include"),

    /**
     * Order by the last modification date.
     */
    MODIFIED("modified"),

    /**
     * Order by the parent resource identifier.
     */
    PARENT("parent"),

    /**
     * Order by search relevance.
     *
     * <p>This option is only meaningful when a search term is provided.</p>
     */
    RELEVANCE("relevance"),

    /**
     * Order by the resource slug.
     */
    SLUG("slug"),

    /**
     * Preserve the order of the slugs specified in the slug parameter.
     */
    INCLUDE_SLUGS("include_slugs"),

    /**
     * Order by the rendered title.
     */
    TITLE("title"),
    /**
     * Order by the menu_order.
     */
    MENU_ORDER("menu_order");

    /**
     * The API value associated with the enum constant.
     * <p>
     * This value is used for JSON serialization when interacting with the WordPress REST API.
     * </p>
     */
    @JsonValue
    private final String value;
}
