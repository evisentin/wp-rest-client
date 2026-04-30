package com.ev.wordpress.client.domain.exception;

import com.ev.wordpress.client.domain.model.WpError;
import lombok.Getter;

/**
 * Exception thrown when the WordPress REST API returns a 404 Not Found response.
 * <p>
 * This typically indicates that the requested resource does not exist or is not accessible.
 * </p>
 */
@Getter
public class WpNotFoundException extends WpException {

    /**
     * Creates a new exception for a 404 Not Found response.
     *
     * @param error
     *         the error returned by the WordPress REST API
     */
    public WpNotFoundException(final WpError error) {
        super(error);
    }
}
