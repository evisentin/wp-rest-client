package io.github.evisentin.wordpress.client.domain.auth.jwt;

import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;

/**
 * Client abstraction responsible for obtaining JWT tokens for a WordPress authentication strategy.
 */
public interface JwtTokenClient {

    /**
     * Fetches a JWT token using the supplied authentication strategy.
     *
     * @param strategy
     *         JWT authentication strategy containing credentials and token URL
     *
     * @return JWT token response
     */
    JwtResponse fetchToken(WpJwtAuthenticationStrategy strategy);
}
