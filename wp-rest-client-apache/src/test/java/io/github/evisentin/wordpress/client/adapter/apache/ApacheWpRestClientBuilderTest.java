package io.github.evisentin.wordpress.client.adapter.apache;

import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

class ApacheWpRestClientBuilderTest implements WithAssertions {

    private static final String BASE_URL = "https://example.com";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";
    private static final String JWT_TOKEN_URL = "https://example.com/wp-json/jwt-auth/v1/token";

    @Test
    void shouldAllowRequestInterceptor() {
        HttpRequestInterceptor interceptor = (request, entity, context) -> {};

        assertThatCode(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowResponseInterceptor() {
        HttpResponseInterceptor interceptor = (response, entity, context) -> {};

        assertThatCode(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowSslConfiguration() {
        SslConfiguration sslConfiguration = SslConfiguration.builder().trustManager(new TestX509TrustManager()).build();

        assertThatCode(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withSslConfiguration(sslConfiguration)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowTimeoutConfiguration() {
        TimeoutConfiguration timeoutConfiguration = TimeoutConfiguration.builder().build();

        assertThatCode(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withTimeoutConfiguration(timeoutConfiguration)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldBuildClientWithBasicAuthentication() {
        ApacheWpRestClient client =
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .build();

        assertThat(client).isNotNull();
    }

    @Test
    void shouldBuildClientWithJwtAuthentication() {
        ApacheWpRestClient client =
                ApacheWpRestClientBuilder
                        .jwtAuthentication(BASE_URL, USERNAME, PASSWORD, JWT_TOKEN_URL)
                        .build();

        assertThat(client).isNotNull();
    }

    @Test
    void shouldRejectBlankBaseUrlWhenUsingBasicAuthentication() {
        assertThatThrownBy(() -> ApacheWpRestClientBuilder.basicAuthentication(" ", USERNAME, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUrl cannot be blank");
    }

    @Test
    void shouldRejectBlankBaseUrlWhenUsingJwtAuthentication() {
        assertThatThrownBy(() -> ApacheWpRestClientBuilder.jwtAuthentication(" ", USERNAME, PASSWORD, JWT_TOKEN_URL))
                .isInstanceOf(IllegalArgumentException.class)

                .hasMessage("baseUrl cannot be blank");
    }

    @Test
    void shouldRejectNullBasicAuthenticationArguments() {
        assertThatThrownBy(() -> ApacheWpRestClientBuilder.basicAuthentication(null, USERNAME, PASSWORD))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");

        assertThatThrownBy(() -> ApacheWpRestClientBuilder.basicAuthentication(BASE_URL, null, PASSWORD))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username is marked non-null but is null");

        assertThatThrownBy(() -> ApacheWpRestClientBuilder.basicAuthentication(BASE_URL, USERNAME, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");
    }

    @Test
    void shouldRejectNullJwtAuthenticationArguments() {
        assertThatThrownBy(() -> ApacheWpRestClientBuilder.jwtAuthentication(null, USERNAME, PASSWORD, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");

        assertThatThrownBy(() -> ApacheWpRestClientBuilder.jwtAuthentication(BASE_URL, null, PASSWORD, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username is marked non-null but is null");

        assertThatThrownBy(() -> ApacheWpRestClientBuilder.jwtAuthentication(BASE_URL, USERNAME, null, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");

        assertThatThrownBy(() -> ApacheWpRestClientBuilder.jwtAuthentication(BASE_URL, USERNAME, PASSWORD, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jwtTokenUrl is marked non-null but is null");
    }

    @Test
    void shouldRejectNullRequestInterceptor() {
        HttpRequestInterceptor interceptor = null;

        assertThatThrownBy(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("interceptor is marked non-null but is null");
    }

    @Test
    void shouldRejectNullResponseInterceptor() {
        HttpResponseInterceptor interceptor = null;

        assertThatThrownBy(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("interceptor is marked non-null but is null");
    }

    @Test
    void shouldRejectNullSslConfiguration() {
        SslConfiguration sslConfiguration = null;

        assertThatThrownBy(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withSslConfiguration(sslConfiguration)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sslConfiguration is marked non-null but is null");
    }

    @Test
    void shouldRejectNullTimeoutConfiguration() {
        TimeoutConfiguration timeoutConfiguration = null;

        assertThatThrownBy(() ->
                ApacheWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withTimeoutConfiguration(timeoutConfiguration)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("timeoutConfiguration is marked non-null but is null");
    }

    /**
     * Insecure {@link X509TrustManager} implementation for testing purposes.
     *
     * <p>This trust manager performs no validation of client or server
     * certificates, effectively trusting all certificates presented during SSL/TLS handshakes.</p>
     *
     * <p>It is intended solely for use in test environments where certificate
     * validation would otherwise fail (e.g. self-signed certificates in containerized setups).</p>
     *
     * <p><strong>Security warning:</strong> This implementation disables all
     * certificate validation and must never be used in production code.</p>
     *
     * <h2>Behavior</h2>
     * <ul>
     *   <li>{@link #checkClientTrusted(X509Certificate[], String)} - no-op</li>
     *   <li>{@link #checkServerTrusted(X509Certificate[], String)} - no-op</li>
     *   <li>{@link #getAcceptedIssuers()} - returns an empty array</li>
     * </ul>
     */
    public static class TestX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
