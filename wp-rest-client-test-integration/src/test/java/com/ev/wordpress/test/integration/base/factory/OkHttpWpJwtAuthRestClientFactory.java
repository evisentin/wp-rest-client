package com.ev.wordpress.test.integration.base.factory;

import com.ev.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;

public final class OkHttpWpJwtAuthRestClientFactory implements WpJwtAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public OkHttpWpJwtAuthRestClientFactory(SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(String baseUrl, String jwtTokenUrl, String username, String password) {
        return new OkHttpWpRestClient(
                baseUrl,
                new WpJwtAuthenticationStrategy(username, password, jwtTokenUrl),
                sslConfiguration,
                null
        );
    }
}
