package io.github.evisentin.wordpress.rest.client.domain.auth.header;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtTokenClient;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.TokenUtils;
import lombok.NonNull;

import java.time.Instant;

/**
 * Creates JWT bearer {@code Authorization} headers.
 *
 * <p>This provider fetches a JWT token using a {@link JwtTokenClient}, caches it, and reuses it until it expires.
 * Token refresh is synchronized to avoid duplicate token requests when accessed concurrently.</p>
 */
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

            token = response.getToken();
            expiresAt = TokenUtils.resolveExpiration(token);

            return bearerToken();
        }
    }

    private String bearerToken() {
        return String.format("Bearer %s", token);
    }

    private boolean hasValidToken() {
        return (token != null) && !TokenUtils.isExpired(expiresAt);
    }
}
