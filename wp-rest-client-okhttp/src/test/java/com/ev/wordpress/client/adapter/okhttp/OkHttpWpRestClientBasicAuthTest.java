package com.ev.wordpress.client.adapter.okhttp;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.testsupport.AbstractBasicAuthenticationWpRestClientContractTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

class OkHttpWpRestClientBasicAuthTest extends AbstractBasicAuthenticationWpRestClientContractTest {

    @Test
    void constructorFailsOnInvalidSSLConfiguration() {

        final String baseUrl = "http://localhost:8080";
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

        final SslConfiguration sslConfiguration = SslConfiguration.builder()
                                                                  .sslSocketFactory(null)
                                                                  .trustManager(null)
                                                                  .build();

        assertThatThrownBy(() -> new OkHttpWpRestClient(baseUrl, authenticationStrategy, sslConfiguration))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("SSL configuration requires both sslSocketFactory and trustManager");
    }

    @Test
    void constructorFailsOnNullParameters() {
        assertThatThrownBy(() -> new OkHttpWpRestClient(null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");
        assertThatThrownBy(() -> new OkHttpWpRestClient("http://localhost:8080", null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authenticationStrategy is marked non-null but is null");
    }

    @Override
    @SneakyThrows
    protected WpBaseRestClient client() {

        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

        return new OkHttpWpRestClient(mockServerUrl(), authenticationStrategy, testSSLConfiguration());
    }

    @SneakyThrows
    private static SslConfiguration testSSLConfiguration() {
        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        SslConfiguration sslConfig = SslConfiguration.builder()
                                                     .sslSocketFactory(sslSocketFactory)
                                                     .trustManager(trustManager)
                                                     .hostnameVerifier((h, s) -> true)
                                                     .build();
        return sslConfig;
    }
}
