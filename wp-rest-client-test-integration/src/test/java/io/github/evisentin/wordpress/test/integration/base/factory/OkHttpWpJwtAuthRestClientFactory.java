package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;

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
