package com.ev.wordpress.client.domain.exception;

import com.ev.wordpress.client.domain.dto.WpError;
import lombok.Getter;

import java.util.Optional;

/**
 * Base class for exceptions thrown when the WordPress REST API returns an error response.
 * <p>
 * This exception stores the {@link WpError} payload returned by WordPress and uses its message as the exception message
 * when available.
 * </p>
 */
@Getter
abstract class WpException extends RuntimeException {

    /**
     * The error payload returned by the WordPress REST API.
     */
    private final WpError error;

    /**
     * Creates a new exception wrapping the WordPress error payload.
     *
     * @param error
     *         the error returned by the WordPress REST API; may be {@code null}
     */
    protected WpException(final WpError error) {
        super(Optional.ofNullable(error).map(WpError::getMessage).orElse(""));
        this.error = error;
    }
}
