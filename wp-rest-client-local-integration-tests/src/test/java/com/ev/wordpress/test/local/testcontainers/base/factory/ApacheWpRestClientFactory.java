package com.ev.wordpress.test.local.testcontainers.base.factory;

import com.ev.wordpress.client.adapter.apache.ApacheWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;

public final class ApacheWpRestClientFactory implements WpRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public ApacheWpRestClientFactory(SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(String baseUrl, String username, String password) {
        return new ApacheWpRestClient(
                baseUrl,
                new WpBasicAuthenticationStrategy(username, password),
                sslConfiguration,
                null
        );
    }
}
