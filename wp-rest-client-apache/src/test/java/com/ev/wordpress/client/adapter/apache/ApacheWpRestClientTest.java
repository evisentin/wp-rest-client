package com.ev.wordpress.client.adapter.apache;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import com.ev.wordpress.client.test.commons.ssl.TestX509TrustManager;
import com.ev.wordpress.client.test.support.AbstractBasicAuthenticationWpRestClientContractTest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

class ApacheWpRestClientTest extends AbstractBasicAuthenticationWpRestClientContractTest {

    @Override
    protected WpBaseRestClient client() {
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

        return new ApacheWpRestClient(mockServerUrl(), authenticationStrategy, insecure(), null);
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
