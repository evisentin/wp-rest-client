package io.github.evisentin.wordpress.client.adapter.apache.auth.jwt;

import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ApacheJwtAuthenticationStrategyHandlerTest implements WithAssertions {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";

    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @Test
    void shouldAuthenticateJwtStrategy() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        {
                          "jwt_token": "jwt-token",
                          "user_email": "admin@example.com",
                          "user_nicename": "admin",
                          "user_display_name": "Admin"
                        }
                        """));

        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault());

        String authorizationHeader = handler.authenticateTyped(strategy());

        assertThat(authorizationHeader).isEqualTo("Bearer jwt-token");

        var request = server.takeRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/wp-json/jwt-auth/v1/token");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getHeader("Content-Type")).contains("application/x-www-form-urlencoded");
        assertThat(request.getBody().readUtf8())
                .contains("username=" + USERNAME)
                .contains("password=" + PASSWORD);
    }

    @Test
    void shouldRejectNullHttpClient() {
        assertThatThrownBy(() -> new ApacheJwtAuthenticationStrategyHandler(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {
        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault());

        assertThatThrownBy(() -> handler.authenticateTyped(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldSupportJwtAuthenticationStrategy() {
        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault());

        assertThat(handler.supports()).isEqualTo(WpJwtAuthenticationStrategy.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private WpJwtAuthenticationStrategy strategy() {
        return new WpJwtAuthenticationStrategy(
                USERNAME,
                PASSWORD,
                server.url("/wp-json/jwt-auth/v1/token").toString()
        );
    }
}
