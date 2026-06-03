package io.github.evisentin.wordpress.client.adapter.okhttp.auth.basic;

import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OkHttpBasicAuthenticationStrategyHandlerTest implements WithAssertions {

    private OkHttpBasicAuthenticationStrategyHandler strategyHandler;

    @Test
    void authenticateTyped__fails_on_null_parameter() {
        assertThatThrownBy(() -> strategyHandler.authenticateTyped(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void authenticateTyped__works() {
        // GIVEN
        WpBasicAuthenticationStrategy strategy = new WpBasicAuthenticationStrategy("my-user", ",y-password");

        // WHEN
        final String header = strategyHandler.authenticateTyped(strategy);

        // THEN
        assertThat(header).isEqualTo("Basic bXktdXNlcjoseS1wYXNzd29yZA==");
    }

    @BeforeEach
    void beforeEach() {
        strategyHandler = new OkHttpBasicAuthenticationStrategyHandler();
    }

    @Test
    void supports__works() {
        assertThat(strategyHandler.supports())
                .isAssignableFrom(WpBasicAuthenticationStrategy.class)
                .isAssignableTo(WpBasicAuthenticationStrategy.class);
    }
}
