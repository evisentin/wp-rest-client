package io.github.evisentin.wordpress.rest.client.domain.exception;

import io.github.evisentin.wordpress.rest.client.domain.model.WpError;
import lombok.Getter;

/**
 * Exception thrown when the WordPress REST API returns a 401 Unauthorized response.
 * <p>
 * This typically indicates that authentication is required, missing, invalid, or expired.
 * </p>
 */
@Getter
public class WpUnauthorizedException extends WpException {

    /**
     * Creates a new exception for a 401 Unauthorized response.
     *
     * @param error
     *         the error returned by the WordPress REST API
     */
    public WpUnauthorizedException(final WpError error) {
        super(error);
    }
}
