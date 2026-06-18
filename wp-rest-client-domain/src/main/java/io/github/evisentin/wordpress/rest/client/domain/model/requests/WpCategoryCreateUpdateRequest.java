package io.github.evisentin.wordpress.rest.client.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Request body used to create or update a WordPress category.
 *
 * <p>This model maps the JSON arguments accepted by the WordPress Categories REST API endpoint, including the category
 * name, description, slug, parent category, and custom meta fields.</p>
 *
 * <p>Null values are omitted during serialization.</p>
 *
 * @see <a href="https://developer.wordpress.org/rest-api/reference/categories/#arguments-2">WordPress REST API -
 * Create a Category Arguments</a>
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpCategoryCreateUpdateRequest {
    /**
     * Human-readable description of the category.
     */
    private String description;

    /**
     * Display name of the category.
     */
    private String name;

    /**
     * The parent item ID.
     */
    @JsonProperty("parent")
    private Long parentId;

    /**
     * URL-friendly slug of the category.
     */
    private String slug;

    @JsonAnySetter
    @Builder.Default
    private Map<String, Object> meta = new HashMap<>();
}
