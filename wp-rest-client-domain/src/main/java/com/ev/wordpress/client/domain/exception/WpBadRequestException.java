package com.ev.wordpress.client.domain.exception;

import com.ev.wordpress.client.domain.model.WpError;
import lombok.Getter;

/**
 * Exception thrown when the WordPress REST API returns a 400 Bad Request response.
 * <p>
 * This typically indicates that the request sent to the API is invalid, malformed, or contains incorrect parameters,
 * such as missing required fields, invalid values, or failed validation.
 * </p>
 */
@Getter
public class WpBadRequestException extends WpException {

    /**
     * Creates a new exception for a 400 Bad Request response.
     *
     * @param error
     *         the error returned by the WordPress REST API
     */
    public WpBadRequestException(final WpError error) {
        super(error);
    }
}
