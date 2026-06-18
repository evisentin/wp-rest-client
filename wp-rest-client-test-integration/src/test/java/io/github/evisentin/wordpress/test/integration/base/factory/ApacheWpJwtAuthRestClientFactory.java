package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.rest.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.configuration.SslConfiguration;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;

public final class ApacheWpJwtAuthRestClientFactory implements WpJwtAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public ApacheWpJwtAuthRestClientFactory(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(final @NonNull String baseUrl,
                               final @NonNull String jwtTokenEndPoint,
                               final @NonNull String username,
                               final @NonNull String password) {
        return ApacheWpRestClientBuilder.jwtAuthentication(baseUrl, username, password, jwtTokenEndPoint)
                                        .withSslConfiguration(sslConfiguration)
                                        .build();
    }
}
