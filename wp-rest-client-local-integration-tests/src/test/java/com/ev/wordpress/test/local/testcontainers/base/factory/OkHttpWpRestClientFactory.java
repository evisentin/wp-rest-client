package com.ev.wordpress.test.local.testcontainers.base.factory;

import com.ev.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;

public final class OkHttpWpRestClientFactory implements WpRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public OkHttpWpRestClientFactory(SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(String baseUrl, String username, String password) {
        return new OkHttpWpRestClient(
                baseUrl,
                new WpBasicAuthenticationStrategy(username, password),
                sslConfiguration,
                null
        );
    }
}
