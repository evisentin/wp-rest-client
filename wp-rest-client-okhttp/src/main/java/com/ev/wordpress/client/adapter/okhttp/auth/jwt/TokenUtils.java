package com.ev.wordpress.client.adapter.okhttp.auth.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {
    // Heuristic threshold: anything above this is treated as a timestamp
    private static final long TIMESTAMP_THRESHOLD = 1_000_000_000L;

    public static boolean isExpired(Instant expiration) {
        return expiration != null && Instant.now().isAfter(expiration);
    }

    public static Instant resolveExpiration(final @NonNull Long expiresIn, final @NonNull Long iat) {

        // Case 1: looks like a timestamp
        if (expiresIn > TIMESTAMP_THRESHOLD) {
            return Instant.ofEpochSecond(expiresIn);
        }

        // Case 2: looks like a duration (seconds)
        return Instant.ofEpochSecond(iat + expiresIn);
    }
}
