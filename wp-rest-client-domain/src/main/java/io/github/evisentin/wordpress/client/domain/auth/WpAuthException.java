package io.github.evisentin.wordpress.client.domain.auth;

import lombok.NonNull;

public class WpAuthException extends RuntimeException {
    public WpAuthException(final @NonNull String message) {
        super(message);
    }

    public WpAuthException(final @NonNull String message, final @NonNull Throwable cause) {
        super(message, cause);
    }
}
