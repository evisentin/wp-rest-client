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
 * @param jwtTokenEndPoint
 *         endpoint for the JWT token, relative to API-URL; must not be blank
 */
public record WpJwtAuthenticationStrategy(@NonNull String username,
                                          @NonNull String password,
                                          @NonNull String jwtTokenEndPoint) implements WpAuthenticationStrategy {
    public WpJwtAuthenticationStrategy {
        if (username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password.isBlank()) throw new IllegalArgumentException("password must not be blank");
        if (jwtTokenEndPoint.isBlank()) throw new IllegalArgumentException("jwtTokenEndPoint must not be blank");
    }
}
