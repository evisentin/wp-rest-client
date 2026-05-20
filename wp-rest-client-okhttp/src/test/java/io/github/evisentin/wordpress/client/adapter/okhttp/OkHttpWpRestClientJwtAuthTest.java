package io.github.evisentin.wordpress.client.adapter.okhttp;

import io.github.evisentin.wordpress.client.domain.api.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.test.support.AbstractJwtAuthenticationWpRestClientContractTest;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

class OkHttpWpRestClientJwtAuthTest extends AbstractJwtAuthenticationWpRestClientContractTest {

    @Test
    void constructorFailsOnInvalidSSLConfiguration() {

        final String baseUrl = "http://localhost:8080";
        final String jwtTokenUrl = "http://localhost:8080/wp-json/api/v1/token";
        final WpJwtAuthenticationStrategy authenticationStrategy = new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl);

        final SslConfiguration sslConfiguration = SslConfiguration.builder()
                                                                  .sslSocketFactory(null)
                                                                  .trustManager(null)
                                                                  .build();

        assertThatThrownBy(() -> new OkHttpWpRestClient(baseUrl, authenticationStrategy, sslConfiguration, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("SSL configuration requires both sslSocketFactory and trustManager");
    }

    @Test
    void constructorFailsOnNullParameters() {
        assertThatThrownBy(() -> new OkHttpWpRestClient(null, null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");
        assertThatThrownBy(() -> new OkHttpWpRestClient("http://localhost:8080", null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authenticationStrategy is marked non-null but is null");
    }

    @Test
    void constructorWorksOnNoSSL() {

        final String baseUrl = "http://localhost:8080";
        final String jwtTokenUrl = "http://localhost:8080/wp-json/api/v1/token";
        final WpJwtAuthenticationStrategy authenticationStrategy = new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl);

        val client = new OkHttpWpRestClient(baseUrl, authenticationStrategy, null, null);

        assertThat(client).isNotNull();
    }

    @Test
    @SneakyThrows
    void constructorWorksWithSSLConfigurationAndNoHostNameVerifier() {

        final String baseUrl = "http://localhost:8080";
        final String jwtTokenUrl = "http://localhost:8080/wp-json/api/v1/token";
        final WpJwtAuthenticationStrategy authenticationStrategy = new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl);

        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        SslConfiguration sslConfiguration = SslConfiguration.builder()
                                                            .sslSocketFactory(sslSocketFactory)
                                                            .trustManager(trustManager)
                                                            //.hostnameVerifier((h, s) -> true)
                                                            .build();

        val client = new OkHttpWpRestClient(baseUrl, authenticationStrategy, sslConfiguration, null);
        assertThat(client).isNotNull();
    }

    @Test
    @SneakyThrows
    void constructorWorksWithSSLConfigurationHavingHostNameVerifier() {

        final String baseUrl = "http://localhost:8080";
        final String jwtTokenUrl = "http://localhost:8080/wp-json/api/v1/token";
        final WpJwtAuthenticationStrategy authenticationStrategy = new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl);

        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        SslConfiguration sslConfiguration = SslConfiguration.builder()
                                                            .sslSocketFactory(sslSocketFactory)
                                                            .trustManager(trustManager)
                                                            .hostnameVerifier((h, s) -> true)
                                                            .build();

        val client = new OkHttpWpRestClient(baseUrl, authenticationStrategy, sslConfiguration, null);
        assertThat(client).isNotNull();
    }

    @Override
    @SneakyThrows
    protected WpBaseRestClient client() {

        final String jwtTokenUrl = mockServerUrl() + "/wp-json/api/v1/token";
        final WpJwtAuthenticationStrategy authenticationStrategy = new WpJwtAuthenticationStrategy("user", "password", jwtTokenUrl);

        return new OkHttpWpRestClient(mockServerUrl(), authenticationStrategy, testSSLConfiguration(), null);
    }

    @SneakyThrows
    private static SslConfiguration testSSLConfiguration() {
        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        return SslConfiguration.builder()
                               .sslSocketFactory(sslSocketFactory)
                               .trustManager(trustManager)
                               .hostnameVerifier((h, s) -> true)
                               .build();
    }
}
