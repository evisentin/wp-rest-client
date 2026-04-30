package com.ev.wordpress.client.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Represents an error response returned by the WordPress REST API.
 *
 * <p>This model contains the error code, a human-readable message, and an
 * optional data object with additional details.</p>
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WpError {

    /**
     * Machine-readable error code.
     */
    private String code;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Additional error details returned by the API.
     */
    private Map<String, Object> data;
}
