package io.github.evisentin.wordpress.client.domain.model.requests;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WpTagCreateUpdateRequest {
    /**
     * Human-readable description of the tag.
     */
    private String description;

    /**
     * Display name of the tag.
     */
    private String name;

    /**
     * URL-friendly slug of the tag.
     */
    private String slug;

    @JsonAnySetter
    @Builder.Default
    private Map<String, Object> meta = new HashMap<>();
}
