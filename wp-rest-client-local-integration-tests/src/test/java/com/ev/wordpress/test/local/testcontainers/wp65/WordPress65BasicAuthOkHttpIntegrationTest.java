package com.ev.wordpress.test.local.testcontainers.wp65;

import com.ev.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import com.ev.wordpress.test.local.testcontainers.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.utils.TestX509TrustManager;
import lombok.SneakyThrows;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;

public class WordPress65BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpRestClient initAdminClient() {
        return new OkHttpWpRestClient(getHttpsBaseUrl(),
                new WpBasicAuthenticationStrategy(WP_ADMIN_USER_NAME, adminApplicationPassword),
                testSSLConfiguration(), null);
    }

    @Override
    protected WpRestClient initStandardUserClient() {
        return new OkHttpWpRestClient(getHttpsBaseUrl(),
                new WpBasicAuthenticationStrategy(WP_STANDARD_USER_NAME, WP_STANDARD_USER_PASSWORD),
                testSSLConfiguration(), null);
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
