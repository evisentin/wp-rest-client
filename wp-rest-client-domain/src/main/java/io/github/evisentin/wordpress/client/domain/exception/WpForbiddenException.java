package io.github.evisentin.wordpress.client.domain.exception;

import io.github.evisentin.wordpress.client.domain.model.WpError;
import lombok.Getter;

/**
 * Exception thrown when the WordPress REST API returns a 403 Forbidden response.
 * <p>
 * This typically indicates that the request was understood, but the authenticated user does not have sufficient
 * permissions to perform the requested operation.
 * </p>
 */
@Getter
public class WpForbiddenException extends WpException {

    /**
     * Creates a new exception for a 403 Forbidden response.
     *
     * @param error
     *         the error returned by the WordPress REST API
     */
    public WpForbiddenException(final WpError error) {
        super(error);
    }
}
