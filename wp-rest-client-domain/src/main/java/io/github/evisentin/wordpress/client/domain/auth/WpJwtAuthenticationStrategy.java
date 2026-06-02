package io.github.evisentin.wordpress.client.domain.auth;

import lombok.NonNull;

/**
 * Authentication strategy based on JWT bearer token authentication.
 *
 * <p>Contains the credentials used to obtain a JWT token and the endpoint from which the token is requested.</p>
 *
 * @param username
 *         WordPress username; must not be blank
 * @param password
 *         WordPress password; must not be blank
 * @param jwtTokenUrl
 *         URL of the JWT token endpoint; must not be blank
 */
public record WpJwtAuthenticationStrategy(@NonNull String username,
                                          @NonNull String password,
                                          @NonNull String jwtTokenUrl) implements WpAuthenticationStrategy {
    public WpJwtAuthenticationStrategy {
        if (username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password.isBlank()) throw new IllegalArgumentException("password must not be blank");
        if (jwtTokenUrl.isBlank()) throw new IllegalArgumentException("jwtTokenUrl must not be blank");
    }
}
