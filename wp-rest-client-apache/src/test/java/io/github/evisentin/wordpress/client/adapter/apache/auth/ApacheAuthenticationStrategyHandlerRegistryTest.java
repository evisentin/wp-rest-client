package io.github.evisentin.wordpress.client.adapter.apache.auth;

import io.github.evisentin.wordpress.client.adapter.apache.auth.basic.ApacheBasicAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.adapter.apache.auth.jwt.ApacheJwtAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApacheAuthenticationStrategyHandlerRegistryTest implements WithAssertions {

    @Mock
    private CloseableHttpClient httpClient;

    private ApacheAuthenticationStrategyHandlerRegistry registry;

    @BeforeEach
    void beforeEach() {
        registry = new ApacheAuthenticationStrategyHandlerRegistry(httpClient, "");
    }

    @Test
    void shouldFailOnUnknownStrategy() {

        assertThatThrownBy(() -> registry.getHandler(new WpAuthenticationStrategy() {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No handler found");
    }

    @Test
    void shouldRejectNullParameters() {
        assertThatThrownBy(() -> new ApacheAuthenticationStrategyHandlerRegistry(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new ApacheAuthenticationStrategyHandlerRegistry(HttpClients.createDefault(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {

        assertThatThrownBy(() -> registry.getHandler(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldReturnBasicAuthenticationHandler() {

        var handler = registry.getHandler(new WpBasicAuthenticationStrategy("user", "password"));

        assertThat(handler)
                .isNotNull()
                .isInstanceOf(ApacheBasicAuthenticationStrategyHandler.class);
    }

    @Test
    void shouldReturnJwtAuthenticationHandler() {
        var handler = registry.getHandler(
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/token"
                )
        );

        assertThat(handler)
                .isNotNull()
                .isInstanceOf(ApacheJwtAuthenticationStrategyHandler.class);
    }
}
