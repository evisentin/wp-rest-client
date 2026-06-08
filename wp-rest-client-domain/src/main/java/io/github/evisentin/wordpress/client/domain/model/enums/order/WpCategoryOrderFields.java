package io.github.evisentin.wordpress.client.domain.model.enums.order;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.evisentin.wordpress.client.domain.model.enums.WpHasValueEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines the fields that can be used to sort WordPress category query results.
 *
 * <p>These values map to the {@code orderby} argument supported by the WordPress Categories REST API endpoint.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/categories/#arguments">WordPress REST API -
 * Categories Arguments</a>
 */
@Getter
@RequiredArgsConstructor
public enum WpCategoryOrderFields implements WpHasValueEnum {

    /**
     * Order by the term identifier.
     */
    ID("id"),

    /**
     * Preserve the order of the identifiers specified in the include parameter.
     */
    INCLUDE("include"),

    /**
     * Order by the term name.
     */
    NAME("name"),

    /**
     * Order by the term slug.
     */
    SLUG("slug"),

    /**
     * Preserve the order of the slugs specified in the slug parameter.
     */
    INCLUDE_SLUGS("include_slugs"),

    /**
     * Order by the term group.
     */
    TERM_GROUP("term_group"),

    /**
     * Order by the term description.
     */
    DESCRIPTION("description"),

    /**
     * Order by the number of resources associated with the term.
     */
    COUNT("count");

    /**
     * The API value associated with the enum constant.
     * <p>
     * This value is used for JSON serialization when interacting with the WordPress REST API.
     * </p>
     */
    @JsonValue
    private final String value;
}
