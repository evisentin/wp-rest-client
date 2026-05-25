package io.github.evisentin.wordpress.client.domain.auth;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class WpJwtAuthenticationStrategyTest implements WithAssertions {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankJwtTokenUrl(String jwtTokenUrl) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("jwtTokenUrl must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankPassword(String password) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy("user", password, "https://example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankUsername(String username) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy(username, "password", "https://example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username must not be blank");
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullJwtTokenUrl(String jwtTokenUrl) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullPassword(String password) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy("user", password, "https://example.com"))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullUsername(String username) {
        assertThatThrownBy(() ->
                new WpJwtAuthenticationStrategy(username, "password", "https://example.com"))
                .isInstanceOf(NullPointerException.class);
    }
}
