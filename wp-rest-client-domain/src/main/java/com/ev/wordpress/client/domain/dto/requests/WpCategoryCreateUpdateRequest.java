package com.ev.wordpress.client.domain.dto.requests;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(setterPrefix = "with", toBuilder = true)
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
