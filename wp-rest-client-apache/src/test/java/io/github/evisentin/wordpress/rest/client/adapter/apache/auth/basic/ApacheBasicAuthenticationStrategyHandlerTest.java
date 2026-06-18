package io.github.evisentin.wordpress.rest.client.adapter.apache.auth.basic;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpBasicAuthenticationStrategy;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class ApacheBasicAuthenticationStrategyHandlerTest implements WithAssertions {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";

    private ApacheBasicAuthenticationStrategyHandler handler;

    @BeforeEach
    void beforeEach() {
        handler = new ApacheBasicAuthenticationStrategyHandler();
    }

    @Test
    void shouldAuthenticateBasicStrategy() {

        String authorizationHeader = handler.authenticateTyped(strategy());

        assertThat(authorizationHeader)
                .isEqualTo("Basic " + Base64.getEncoder()
                                            .encodeToString((USERNAME + ":" + PASSWORD)
                                                    .getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void shouldRejectNullStrategy() {

        assertThatThrownBy(() -> handler.authenticateTyped(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldSupportBasicAuthenticationStrategy() {

        assertThat(handler.supports()).isEqualTo(WpBasicAuthenticationStrategy.class);
    }

    private WpBasicAuthenticationStrategy strategy() {
        return new WpBasicAuthenticationStrategy(USERNAME, PASSWORD);
    }
}
