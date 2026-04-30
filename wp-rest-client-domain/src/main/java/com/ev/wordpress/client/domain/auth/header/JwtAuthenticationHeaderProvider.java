package com.ev.wordpress.client.domain.auth.header;

import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.jwt.JwtResponse;
import com.ev.wordpress.client.domain.auth.jwt.JwtTokenClient;
import com.ev.wordpress.client.domain.auth.jwt.TokenUtils;
import lombok.NonNull;

import java.time.Instant;

public final class JwtAuthenticationHeaderProvider
        implements AuthenticationHeaderProvider<WpJwtAuthenticationStrategy> {

    private final JwtTokenClient tokenClient;
    private final Object lock = new Object();

    private volatile String token;
    private volatile Instant expiresAt;

    public JwtAuthenticationHeaderProvider(final @NonNull JwtTokenClient tokenClient) {
        this.tokenClient = tokenClient;
    }

    @Override
    public String createAuthorizationHeader(final @NonNull WpJwtAuthenticationStrategy strategy) {

        // Fast path: if we already have a valid (non-expired) token,
        // return it immediately without acquiring a lock.
        if (hasValidToken()) {
            return bearerToken();
        }

        synchronized (lock) {
            // Double-check after acquiring the lock to avoid duplicate token fetches.
            // Another thread may have refreshed the token while we were waiting.
            if (hasValidToken()) {
                return bearerToken();
            }

            JwtResponse response = tokenClient.fetchToken(strategy);

            token = response.getJwtToken();
            expiresAt = TokenUtils.resolveExpiration(response.getExpiresIn(), response.getIat());

            return bearerToken();
        }
    }

    private String bearerToken() {
        return "Bearer " + token;
    }

    private boolean hasValidToken() {
        return token != null && expiresAt != null && !TokenUtils.isExpired(expiresAt);
    }
}
