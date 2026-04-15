package com.ev.wordpress.test.local.testcontainers.wp65;

import com.ev.wordpress.client.adapter.apache.ApacheWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import com.ev.wordpress.test.local.testcontainers.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.utils.TestX509TrustManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

public class WordPress65BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpRestClient initAdminClient() {
        return new ApacheWpRestClient(getHttpsBaseUrl(), new WpBasicAuthenticationStrategy(WP_ADMIN_USER_NAME, adminApplicationPassword), insecure(), null);
    }

    @Override
    protected WpRestClient initStandardUserClient() {
        return new ApacheWpRestClient(getHttpsBaseUrl(), new WpBasicAuthenticationStrategy(WP_STANDARD_USER_NAME, WP_STANDARD_USER_PASSWORD), insecure(), null);
    }

    private SslConfiguration insecure() {
        final X509TrustManager trustAllManager = new TestX509TrustManager();

        final HostnameVerifier trustAllHosts = (hostname, session) -> true;

        return SslConfiguration.builder()
                               .trustManager(trustAllManager)
                               .hostnameVerifier(trustAllHosts)
                               .build();
    }
}
