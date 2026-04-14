package com.ev.wordpress.client.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a WordPress field that may contain both raw and rendered values.
 *
 * <p>This structure is commonly used by the WordPress REST API for fields such
 * as titles, excerpts, content, and GUID values.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpRenderedField {

    /**
     * Unrendered raw value, if provided by the API.
     */
    private String raw;

    /**
     * Rendered value returned by the API, typically HTML.
     */
    private String rendered;

    /**
     * Indicates whether the field content is protected.
     */
    @JsonProperty("protected")
    private Boolean isProtected;

    /**
     * Gutenberg block version associated with the rendered content.
     */
    @JsonProperty("block_version")
    private Integer blockVersion;
}
