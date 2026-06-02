
package io.github.evisentin.wordpress.client.adapter.okhttp.auth;

import io.github.evisentin.wordpress.client.adapter.okhttp.auth.basic.OkHttpBasicAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.adapter.okhttp.auth.jwt.OkHttpJwtAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import okhttp3.OkHttpClient;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class OkHttpAuthenticationStrategyHandlerRegistryTest implements WithAssertions {

    @Test
    void shouldReturnBasicAuthenticationHandler() {
        OkHttpAuthenticationStrategyHandlerRegistry registry =
                new OkHttpAuthenticationStrategyHandlerRegistry(new OkHttpClient());

        var handler = registry.getHandler(
                new WpBasicAuthenticationStrategy("user", "password")
        );

        assertThat(handler)
                .isNotNull()
                .isInstanceOf(OkHttpBasicAuthenticationStrategyHandler.class);
    }

    @Test
    void shouldReturnJwtAuthenticationHandler() {
        OkHttpAuthenticationStrategyHandlerRegistry registry =
                new OkHttpAuthenticationStrategyHandlerRegistry(new OkHttpClient());

        var handler = registry.getHandler(
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/token"
                )
        );

        assertThat(handler)
                .isNotNull()
                .isInstanceOf(OkHttpJwtAuthenticationStrategyHandler.class);
    }

    @Test
    void shouldFailOnUnknownStrategy() {
        OkHttpAuthenticationStrategyHandlerRegistry registry =
                new OkHttpAuthenticationStrategyHandlerRegistry(new OkHttpClient());

        assertThatThrownBy(() -> registry.getHandler(new WpAuthenticationStrategy() {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No handler found");
    }

    @Test
    void shouldRejectNullOkHttpClient() {
        assertThatThrownBy(() ->
                new OkHttpAuthenticationStrategyHandlerRegistry(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {
        OkHttpAuthenticationStrategyHandlerRegistry registry =
                new OkHttpAuthenticationStrategyHandlerRegistry(new OkHttpClient());

        assertThatThrownBy(() ->
                registry.getHandler(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }
}
