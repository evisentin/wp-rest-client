package com.ev.wordpress.client.domain.auth.jwt;

import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;

public interface JwtTokenClient {
    JwtResponse fetchToken(WpJwtAuthenticationStrategy strategy);
}
