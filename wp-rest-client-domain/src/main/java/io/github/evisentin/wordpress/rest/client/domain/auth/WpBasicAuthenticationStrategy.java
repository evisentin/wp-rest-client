package io.github.evisentin.wordpress.rest.client.domain.auth;

import lombok.NonNull;

/**
 * Authentication strategy based on HTTP Basic Authentication.
 *
 * @param username
 *         WordPress username; must not be blank
 * @param password
 *         WordPress password or application password; must not be blank
 */
public record WpBasicAuthenticationStrategy(@NonNull String username,
                                            @NonNull String password) implements WpAuthenticationStrategy {
    public WpBasicAuthenticationStrategy {
        if (username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password.isBlank()) throw new IllegalArgumentException("password must not be blank");
    }
}
