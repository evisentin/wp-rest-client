package io.github.evisentin.wordpress.client.domain.auth;

public class WpAuthException extends RuntimeException {
    public WpAuthException(String message) {
        super(message);
    }

    public WpAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
