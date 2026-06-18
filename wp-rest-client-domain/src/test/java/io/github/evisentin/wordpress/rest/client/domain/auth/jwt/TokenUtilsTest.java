package io.github.evisentin.wordpress.rest.client.domain.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

class TokenUtilsTest implements WithAssertions {

    @Test
    void isExpiredReturnsFalseWhenExpirationIsInFuture() {
        assertThat(TokenUtils.isExpired(Instant.now().plusSeconds(60))).isFalse();
    }

    @Test
    void isExpiredReturnsFalseWhenExpirationIsNull() {
        assertThat(TokenUtils.isExpired(null)).isFalse();
    }

    @Test
    void isExpiredReturnsTrueWhenExpirationIsInPast() {
        assertThat(TokenUtils.isExpired(Instant.now().minusSeconds(60))).isTrue();
    }

    @Test
    void resolveExpirationFailsOnNullInput() {
        assertThatThrownBy(() -> TokenUtils.resolveExpiration(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jwtToken is marked non-null but is null");
    }

    @Test
    void resolveExpirationReturnsJwtExpiration() {

        final Instant expiration = Instant.parse("2030-01-01T12:00:00Z");

        final String token = JWT.create()
                                .withExpiresAt(Date.from(expiration))
                                .sign(Algorithm.HMAC256("test-secret"));

        assertThat(TokenUtils.resolveExpiration(token))
                .isEqualTo(expiration);
    }
}
