package io.github.evisentin.wordpress.client.domain.auth;

import lombok.NonNull;

/**
 * Runtime exception thrown when WordPress authentication fails or cannot be completed.
 */
public class WpAuthException extends RuntimeException {

    /**
     * Creates a new authentication exception with the specified detail message.
     *
     * @param message
     *         the detail message describing the authentication failure; must not be {@code null}
     */
    public WpAuthException(final @NonNull String message) {
        super(message);
    }

    /**
     * Creates a new authentication exception with the specified detail message and cause.
     *
     * @param message
     *         the detail message describing the authentication failure; must not be {@code null}
     * @param cause
     *         the underlying cause of the authentication failure; must not be {@code null}
     */
    public WpAuthException(final @NonNull String message, final @NonNull Throwable cause) {
        super(message, cause);
    }
}
