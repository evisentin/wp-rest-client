package io.github.evisentin.wordpress.client.domain.auth;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class WpBasicAuthenticationStrategyTest implements WithAssertions {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankPassword(String password) {
        assertThatThrownBy(() ->
                new WpBasicAuthenticationStrategy("user", password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankUsername(String username) {
        assertThatThrownBy(() ->
                new WpBasicAuthenticationStrategy(username, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username must not be blank");
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullPassword(String password) {
        assertThatThrownBy(() ->
                new WpBasicAuthenticationStrategy("user", password))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @NullSource
    void shouldRejectNullUsername(String username) {
        assertThatThrownBy(() ->
                new WpBasicAuthenticationStrategy(username, "password"))
                .isInstanceOf(NullPointerException.class);
    }
}
