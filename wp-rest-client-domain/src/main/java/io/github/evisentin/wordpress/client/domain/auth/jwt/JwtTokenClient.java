package io.github.evisentin.wordpress.client.domain.auth.jwt;

import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;

public interface JwtTokenClient {
    JwtResponse fetchToken(WpJwtAuthenticationStrategy strategy);
}
