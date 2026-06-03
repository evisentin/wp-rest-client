package io.github.evisentin.wordpress.client.adapter.okhttp;

import io.github.evisentin.wordpress.client.contract.test.AbstractMockServerTest;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

class OkHttpWpRestClientBuilderTest extends AbstractMockServerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret";

    private String BASE_URL;
    private String JWT_TOKEN_URL;

    @BeforeEach
    void beforeEach() {
        BASE_URL = mockServerUrl();
        JWT_TOKEN_URL = "/jwt-auth/v1/token";
    }

    @Test
    void shouldAllowInterceptor() {
        Interceptor interceptor = chain -> chain.proceed(chain.request());

        assertThatCode(() ->
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    @SneakyThrows
    void shouldAllowSslConfiguration() {

        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        SslConfiguration sslConfiguration =
                SslConfiguration.builder()
                                .trustManager(trustManager)
                                .sslSocketFactory(sslContext.getSocketFactory())
                                .build();

        assertThatCode(() ->
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withSslConfiguration(sslConfiguration)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowTimeoutConfiguration() {
        TimeoutConfiguration timeoutConfiguration =
                TimeoutConfiguration.builder()
                                    .callTimeout(Duration.of(2, ChronoUnit.MINUTES))
                                    .connectTimeout(Duration.of(2, ChronoUnit.MINUTES))
                                    .readTimeout(Duration.of(2, ChronoUnit.MINUTES))
                                    .writeTimeout(Duration.of(2, ChronoUnit.MINUTES))
                                    .build();

        assertThatCode(() ->
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withTimeoutConfiguration(timeoutConfiguration)
                        .build()
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldBuildClientWithBasicAuthentication() {
        OkHttpWpRestClient client =
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .build();

        assertThat(client).isNotNull();
    }

    @Test
    void shouldBuildClientWithJwtAuthentication() {
        OkHttpWpRestClient client =
                OkHttpWpRestClientBuilder
                        .jwtAuthentication(BASE_URL, USERNAME, PASSWORD, JWT_TOKEN_URL)
                        .build();

        assertThat(client).isNotNull();
    }

    @Test
    void shouldRejectBlankBaseUrlWhenUsingBasicAuthentication() {
        assertThatThrownBy(() ->
                OkHttpWpRestClientBuilder.basicAuthentication(" ", USERNAME, PASSWORD)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUrl cannot be blank");
    }

    @Test
    void shouldRejectBlankBaseUrlWhenUsingJwtAuthentication() {
        assertThatThrownBy(() ->
                OkHttpWpRestClientBuilder.jwtAuthentication(" ", USERNAME, PASSWORD, JWT_TOKEN_URL)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUrl cannot be blank");
    }

    @Test
    void shouldRejectNullBasicAuthenticationArguments() {
        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.basicAuthentication(null, USERNAME, PASSWORD))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");

        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.basicAuthentication(BASE_URL, null, PASSWORD))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username is marked non-null but is null");

        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.basicAuthentication(BASE_URL, USERNAME, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");
    }

    @Test
    void shouldRejectNullInterceptor() {
        Interceptor interceptor = null;

        assertThatThrownBy(() ->
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withInterceptor(interceptor)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("interceptor is marked non-null but is null");
    }

    @Test
    void shouldRejectNullJwtAuthenticationArguments() {
        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.jwtAuthentication(null, USERNAME, PASSWORD, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");

        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.jwtAuthentication(BASE_URL, null, PASSWORD, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username is marked non-null but is null");

        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.jwtAuthentication(BASE_URL, USERNAME, null, JWT_TOKEN_URL))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("password is marked non-null but is null");

        assertThatThrownBy(() -> OkHttpWpRestClientBuilder.jwtAuthentication(BASE_URL, USERNAME, PASSWORD, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("jwtTokenEndPoint is marked non-null but is null");
    }

    @Test
    void shouldRejectNullSslConfiguration() {
        SslConfiguration sslConfiguration = null;

        assertThatThrownBy(() ->
                OkHttpWpRestClientBuilder
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
                OkHttpWpRestClientBuilder
                        .basicAuthentication(BASE_URL, USERNAME, PASSWORD)
                        .withTimeoutConfiguration(timeoutConfiguration)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("timeoutConfiguration is marked non-null but is null");
    }

    /**
     * Insecure {@link X509TrustManager} implementation for testing purposes.
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
