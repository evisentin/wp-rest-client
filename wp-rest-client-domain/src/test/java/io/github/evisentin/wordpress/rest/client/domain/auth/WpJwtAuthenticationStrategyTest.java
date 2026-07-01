package io.github.evisentin.wordpress.rest.client.domain.auth;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class WpJwtAuthenticationStrategyTest implements WithAssertions {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankJwtTokenEndPoint(String jwtTokenEndPoint) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy("user", "password", jwtTokenEndPoint))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("jwtTokenEndPoint must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankPassword(String password) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy("user", password, "https://example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankUsername(String username) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy(username, "password", "https://example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username must not be blank");
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullJwtTokenEndPoint(String jwtTokenEndPoint) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy("user", "password", jwtTokenEndPoint))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jwtTokenEndPoint is marked non-null but is null");
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullPassword(String password) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy("user", password, "https://example.com"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullUsername(String username) {
        assertThatThrownBy(() -> new WpJwtAuthenticationStrategy(username, "password", "https://example.com"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username is marked non-null but is null");
    }
}
