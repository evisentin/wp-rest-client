package io.github.evisentin.wordpress.rest.client.adapter.okhttp.auth.jwt;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthException;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtResponse;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class OkHttpJwtTokenClientTest implements WithAssertions {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";

    private MockWebServer server;
    private OkHttpJwtTokenClient client;

    @Test
    void constructorsFailOnNullParameter() {
        assertThatThrownBy(() -> new OkHttpJwtTokenClient(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new OkHttpJwtTokenClient(new OkHttpClient(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void fetchTokenFailOnNullParameter() {
        assertThatThrownBy(() -> client.fetchToken(null)).isInstanceOf(NullPointerException.class)
                                                         .hasMessage("strategy is marked non-null but is null");
    }

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();

        client = new OkHttpJwtTokenClient(new OkHttpClient(), "");
    }

    @Test
    void shouldFetchJwtToken() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        {
                          "token": "jwt-token",
                          "user_email": "admin@example.com",
                          "user_nicename": "admin",
                          "user_display_name": "Admin"
                        }
                        """));

        JwtResponse response = client.fetchToken(strategy());

        assertThat(response).isNotNull();

        var request = server.takeRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/jwt/token");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getBody().readUtf8())
                .contains("username=" + USERNAME)
                .contains("password=" + PASSWORD);
    }

    @Test
    void shouldRejectNullParameters() {
        assertThatThrownBy(() -> new OkHttpJwtTokenClient(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new OkHttpJwtTokenClient(new OkHttpClient(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {
        assertThatThrownBy(() -> client.fetchToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldThrowWpAuthExceptionWhenResponseBodyCannotBeParsed() {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("not-json"));

        assertThatThrownBy(() -> client.fetchToken(strategy()))
                .isInstanceOf(WpAuthException.class)
                .hasMessage("Failed to fetch JWT token");
    }

    @Test
    void shouldThrowWpAuthExceptionWhenResponseIsNotSuccessful() {
        server.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("invalid credentials"));

        assertThatThrownBy(() -> client.fetchToken(strategy()))
                .isInstanceOf(WpAuthException.class)
                .hasMessage("Failed to fetch JWT token. HTTP 401: invalid credentials");
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private WpJwtAuthenticationStrategy strategy() {
        return new WpJwtAuthenticationStrategy(
                USERNAME,
                PASSWORD,
                server.url("/jwt/token").toString()
        );
    }
}
