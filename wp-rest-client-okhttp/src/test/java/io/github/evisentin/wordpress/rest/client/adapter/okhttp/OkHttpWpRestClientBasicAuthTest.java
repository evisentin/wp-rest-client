package io.github.evisentin.wordpress.rest.client.adapter.okhttp;

import io.github.evisentin.wordpress.rest.client.contract.test.AbstractBasicAuthenticationWpRestClientContractTest;
import io.github.evisentin.wordpress.rest.client.domain.WpBaseRestClient;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.configuration.SslConfiguration;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

class OkHttpWpRestClientBasicAuthTest extends AbstractBasicAuthenticationWpRestClientContractTest {

    @Test
    void constructorFailsOnInvalidSSLConfiguration() {

        final String baseUrl = mockServerUrl();
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

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

        final String baseUrl = mockServerUrl();
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

        val client = new OkHttpWpRestClient(baseUrl, authenticationStrategy, null, null);

        assertThat(client).isNotNull();
    }

    @Test
    @SneakyThrows
    void constructorWorksWithSSLConfigurationAndNoHostNameVerifier() {

        final String baseUrl = mockServerUrl();
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

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

        final String baseUrl = mockServerUrl();
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

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

        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

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
