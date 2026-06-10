package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.WpRestClient;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;

public final class OkHttpWpJwtAuthRestClientFactory implements WpJwtAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public OkHttpWpJwtAuthRestClientFactory(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(final @NonNull String baseUrl,
                               final @NonNull String jwtTokenEndPoint,
                               final @NonNull String username,
                               final @NonNull String password) {

        return OkHttpWpRestClientBuilder.jwtAuthentication(baseUrl, username, password, jwtTokenEndPoint)
                                        .withSslConfiguration(sslConfiguration)
                                        .build();
    }
}
