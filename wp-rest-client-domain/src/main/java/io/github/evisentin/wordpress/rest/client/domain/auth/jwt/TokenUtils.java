package io.github.evisentin.wordpress.rest.client.domain.auth.jwt;

import com.auth0.jwt.JWT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

/**
 * Utility methods for JWT token expiration handling.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtils {

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

    public static Instant resolveExpiration(final @NonNull String jwtToken) {
        return JWT.decode(jwtToken).getExpiresAt().toInstant();
    }
}
