package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import io.github.evisentin.wordpress.client.domain.api.WpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;

public final class OkHttpWpJwtAuthRestClientFactory implements WpJwtAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public OkHttpWpJwtAuthRestClientFactory(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(final @NonNull String baseUrl,
                               final @NonNull String jwtTokenUrl,
                               final @NonNull String username,
                               final @NonNull String password) {
        return new OkHttpWpRestClient(
                baseUrl,
                new WpJwtAuthenticationStrategy(username, password, jwtTokenUrl),
                sslConfiguration,
                null
        );
    }
}
