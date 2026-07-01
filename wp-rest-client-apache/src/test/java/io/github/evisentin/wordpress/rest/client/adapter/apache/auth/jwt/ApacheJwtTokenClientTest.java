package io.github.evisentin.wordpress.rest.client.adapter.apache.auth.jwt;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthException;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.jwt.JwtResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.apache.hc.core5.http.HttpHeaders.ACCEPT;
import static org.apache.hc.core5.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApacheJwtTokenClientTest implements WithAssertions {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";
    private static final String JWT_TOKEN_URL = "https://example.com/wp-json/jwt-auth/v1/token";

    @Mock
    private CloseableHttpClient httpClient;

    @Test
    void shouldFetchJwtToken() throws Exception {

        ApacheJwtTokenClient client = new ApacheJwtTokenClient(httpClient, "");

        when(httpClient.execute(any(HttpPost.class), any(HttpClientResponseHandler.class)))
                .thenAnswer(invocation -> {
                    HttpClientResponseHandler<?> handler = invocation.getArgument(1);

                    BasicClassicHttpResponse response = new BasicClassicHttpResponse(200);
                    response.setEntity(new StringEntity("""
                            {
                              "token": "jwt-token",
                              "user_email": "admin@example.com",
                              "user_nicename": "admin",
                              "user_display_name": "Admin"
                            }
                            """));

                    return handler.handleResponse(response);
                });

        JwtResponse response = client.fetchToken(strategy());

        assertThat(response).isNotNull();

        ArgumentCaptor<HttpPost> requestCaptor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(requestCaptor.capture(), any(HttpClientResponseHandler.class));

        HttpPost request = requestCaptor.getValue();

        assertThat(request.getUri().toString()).isEqualTo(JWT_TOKEN_URL);
        assertThat(request.getFirstHeader(ACCEPT).getValue()).isEqualTo("application/json");
        assertThat(request.getFirstHeader(CONTENT_TYPE).getValue())
                .isEqualTo("application/x-www-form-urlencoded");

        String requestBody = org.apache.hc.core5.http.io.entity.EntityUtils.toString(request.getEntity());

        assertThat(requestBody)
                .contains("username=" + USERNAME)
                .contains("password=" + PASSWORD);
    }

    @Test
    void shouldRejectNullParameters() {
        assertThatThrownBy(() -> new ApacheJwtTokenClient(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new ApacheJwtTokenClient(HttpClients.createDefault(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void shouldRejectNullStrategy() {

        ApacheJwtTokenClient client = new ApacheJwtTokenClient(httpClient, "");

        assertThatThrownBy(() -> client.fetchToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldThrowWpAuthExceptionWhenHttpClientFails() throws Exception {

        ApacheJwtTokenClient client = new ApacheJwtTokenClient(httpClient, "");

        when(httpClient.execute(any(HttpPost.class), any(HttpClientResponseHandler.class)))
                .thenThrow(new IOException("connection failed"));

        assertThatThrownBy(() -> client.fetchToken(strategy()))
                .isInstanceOf(WpAuthException.class)
                .hasMessage("Failed to fetch JWT token")
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void shouldThrowWpAuthExceptionWhenResponseBodyCannotBeParsed() throws Exception {

        ApacheJwtTokenClient client = new ApacheJwtTokenClient(httpClient, "");

        when(httpClient.execute(any(HttpPost.class), any(HttpClientResponseHandler.class)))
                .thenAnswer(invocation -> {
                    HttpClientResponseHandler<?> handler = invocation.getArgument(1);

                    BasicClassicHttpResponse response = new BasicClassicHttpResponse(200);
                    response.setEntity(new StringEntity("not-json"));

                    return handler.handleResponse(response);
                });

        assertThatThrownBy(() -> client.fetchToken(strategy()))
                .isInstanceOf(WpAuthException.class)
                .hasMessage("Failed to fetch JWT token");
    }

    @Test
    void shouldThrowWpAuthExceptionWhenResponseIsNotSuccessful() throws Exception {

        ApacheJwtTokenClient client = new ApacheJwtTokenClient(httpClient, "");

        when(httpClient.execute(any(HttpPost.class), any(HttpClientResponseHandler.class)))
                .thenAnswer(invocation -> {
                    HttpClientResponseHandler<?> handler = invocation.getArgument(1);

                    BasicClassicHttpResponse response = new BasicClassicHttpResponse(401);
                    response.setEntity(new StringEntity("invalid credentials"));

                    return handler.handleResponse(response);
                });

        assertThatThrownBy(() -> client.fetchToken(strategy()))
                .isInstanceOf(WpAuthException.class)
                .hasMessage("Failed to fetch JWT token. HTTP 401: invalid credentials");
    }

    private WpJwtAuthenticationStrategy strategy() {
        return new WpJwtAuthenticationStrategy(USERNAME, PASSWORD, JWT_TOKEN_URL);
    }
}
