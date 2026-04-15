package com.ev.wordpress.test.local.testcontainers.base.factory;

import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import lombok.SneakyThrows;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

public final class TestSslConfigurations {

    private TestSslConfigurations() {}

    public static SslConfiguration insecureForApache() {
        X509TrustManager trustManager = new TestX509TrustManager();

        return SslConfiguration.builder()
                               .trustManager(trustManager)
                               .hostnameVerifier((h, s) -> true)
                               .build();
    }

    @SneakyThrows
    public static SslConfiguration insecureForOkHttp() {
        X509TrustManager trustManager = new TestX509TrustManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        return SslConfiguration.builder()
                               .sslSocketFactory(sslContext.getSocketFactory())
                               .trustManager(trustManager)
                               .hostnameVerifier((h, s) -> true)
                               .build();
    }
}
