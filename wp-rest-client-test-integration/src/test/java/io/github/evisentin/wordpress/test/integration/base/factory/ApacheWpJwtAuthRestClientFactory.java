package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClient;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;

public final class ApacheWpJwtAuthRestClientFactory implements WpJwtAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public ApacheWpJwtAuthRestClientFactory(SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(String baseUrl, String jwtTokenUrl, String username, String password) {
        return new ApacheWpRestClient(
                baseUrl,
                new WpJwtAuthenticationStrategy(username, password, jwtTokenUrl),
                sslConfiguration,
                null
        );
    }
}
