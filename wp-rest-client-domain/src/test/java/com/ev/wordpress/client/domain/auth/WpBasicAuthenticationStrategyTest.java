package com.ev.wordpress.client.domain.auth;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class WpBasicAuthenticationStrategyTest implements WithAssertions {

    @Test
    void shouldCreateBasicAuthorizationHeader() {
        // given
        String userName = "john";
        String password = "secret";

        // when
        WpBasicAuthenticationStrategy strategy =
                new WpBasicAuthenticationStrategy(userName, password);

        // then
        String expectedToken = getExpectedToken(userName, password);

        assertThat(strategy.getHeaderValue())
                .isEqualTo("Basic " + expectedToken);
    }

    @Test
    void shouldSupportUtf8CharactersInCredentials() {
        // given
        String userName = "jàne";
        String password = "påss";

        // when
        WpBasicAuthenticationStrategy strategy =
                new WpBasicAuthenticationStrategy(userName, password);

        // then
        String expectedToken = getExpectedToken(userName, password);

        assertThat(strategy.getHeaderValue())
                .isEqualTo("Basic " + expectedToken);
    }

    @Test
    void shouldThrowExceptionWhenOnNullParameters() {
        assertThatThrownBy(() -> new WpBasicAuthenticationStrategy(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("userName is marked non-null but is null");

        assertThatThrownBy(() -> new WpBasicAuthenticationStrategy("userName", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank() {
        assertThatThrownBy(() -> new WpBasicAuthenticationStrategy("john", "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password must not be blank");
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsBlank() {
        assertThatThrownBy(() -> new WpBasicAuthenticationStrategy("   ", "secret"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("userName must not be blank");
    }

    private static String getExpectedToken(String userName, String password) {
        return Base64.getEncoder()
                     .encodeToString((userName + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
