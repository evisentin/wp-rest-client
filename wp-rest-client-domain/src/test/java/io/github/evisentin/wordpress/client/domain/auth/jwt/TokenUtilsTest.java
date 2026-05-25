package io.github.evisentin.wordpress.client.domain.auth.jwt;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenUtilsTest {

    @Test
    void shouldRejectNullExpiresIn() {
        assertThatThrownBy(() -> TokenUtils.resolveExpiration(null, 1_700_000_000L))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullIat() {
        assertThatThrownBy(() -> TokenUtils.resolveExpiration(3600L, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldResolveExpirationWhenExpiresInIsDuration() {
        Instant expiration = TokenUtils.resolveExpiration(3600L, 1_700_000_000L);

        assertThat(expiration)
                .isEqualTo(Instant.ofEpochSecond(1_700_003_600L));
    }

    @Test
    void shouldResolveExpirationWhenExpiresInIsTimestamp() {
        Instant expiration = TokenUtils.resolveExpiration(1_700_000_000L, 1_600_000_000L);

        assertThat(expiration)
                .isEqualTo(Instant.ofEpochSecond(1_700_000_000L));
    }

    @Test
    void shouldReturnFalseWhenExpirationIsInTheFuture() {
        assertThat(TokenUtils.isExpired(Instant.now().plusSeconds(60)))
                .isFalse();
    }

    @Test
    void shouldReturnFalseWhenExpirationIsNull() {
        assertThat(TokenUtils.isExpired(null))
                .isFalse();
    }

    @Test
    void shouldReturnTrueWhenExpirationIsInThePast() {
        assertThat(TokenUtils.isExpired(Instant.now().minusSeconds(60)))
                .isTrue();
    }
}
