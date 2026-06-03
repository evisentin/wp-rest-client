package io.github.evisentin.wordpress.client.adapter.apache.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

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

        final String token = createToken();

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        {
                          "jwt_token": "%s",
                          "user_email": "admin@example.com",
                          "user_nicename": "admin",
                          "user_display_name": "Admin"
                        }
                        """.formatted(token)));

        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault(), "");

        String authorizationHeader = handler.authenticateTyped(strategy());

        assertThat(authorizationHeader).isEqualTo("Bearer " + token);

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
    void shouldRejectNullParameters() {
        assertThatThrownBy(() -> new ApacheJwtAuthenticationStrategyHandler(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {
        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault(), "");

        assertThatThrownBy(() -> handler.authenticateTyped(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldSupportJwtAuthenticationStrategy() {
        ApacheJwtAuthenticationStrategyHandler handler =
                new ApacheJwtAuthenticationStrategyHandler(HttpClients.createDefault(), "");

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

    private static String createToken() {
        final Instant now = Instant.now();
        return JWT.create()
                  .withSubject("1")
                  .withClaim("name", "admin")
                  .withIssuedAt(Date.from(now))
                  .withExpiresAt(Date.from(now.plusSeconds(3600L)))
                  .sign(Algorithm.HMAC256("test-secret"));
    }
}
