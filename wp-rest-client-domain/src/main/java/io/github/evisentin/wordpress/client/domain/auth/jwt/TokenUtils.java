package io.github.evisentin.wordpress.client.domain.auth.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

/**
 * Utility methods for JWT token expiration handling.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {
    // Heuristic threshold: anything above this is treated as a timestamp
    private static final long TIMESTAMP_THRESHOLD = 1_000_000_000L;

    /**
     * Determines whether the supplied expiration time has passed.
     *
     * @param expiration
     *         token expiration instant; {@code null} is treated as not expired
     *
     * @return {@code true} if the expiration instant is in the past
     */
    public static boolean isExpired(Instant expiration) {
        return expiration != null && Instant.now().isAfter(expiration);
    }

    /**
     * Resolves a token expiration instant.
     *
     * <p>If {@code expiresIn} looks like an epoch timestamp, it is used directly.
     * Otherwise it is treated as a duration in seconds added to {@code iat}.</p>
     *
     * @param expiresIn
     *         expiration timestamp or duration in seconds
     * @param iat
     *         issued-at timestamp in epoch seconds
     *
     * @return resolved expiration instant
     */

    public static Instant resolveExpiration(final @NonNull Long expiresIn, final @NonNull Long iat) {

        // Case 1: looks like a timestamp
        if (expiresIn > TIMESTAMP_THRESHOLD) {
            return Instant.ofEpochSecond(expiresIn);
        }

        // Case 2: looks like a duration (seconds)
        return Instant.ofEpochSecond(iat + expiresIn);
    }
}
