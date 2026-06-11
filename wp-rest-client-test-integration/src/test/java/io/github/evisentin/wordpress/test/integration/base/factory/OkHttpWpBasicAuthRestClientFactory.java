package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import io.github.evisentin.wordpress.client.adapter.okhttp.OkHttpWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.WpRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.test.integration.base.factory.interceptors.OkHttpTestRequestInterceptor;
import io.github.evisentin.wordpress.test.integration.base.factory.interceptors.OkHttpTestResponseInterceptor;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;

/**
 * {@link WpBasicAuthRestClientFactory} implementation based on OkHttp.
 *
 * <p>This factory creates instances of {@link OkHttpWpRestClient} configured
 * with Basic Authentication and a provided {@link SslConfiguration}.</p>
 *
 * <p>The SSL configuration is supplied externally, enabling flexible test
 * setups such as disabling certificate validation for HTTPS endpoints backed by self-signed certificates.</p>
 *
 * <h2>Authentication</h2>
 * <p>All clients created by this factory use
 * {@link WpBasicAuthenticationStrategy} with the provided credentials.</p>
 *
 * <h2>Use cases</h2>
 * <ul>
 *   <li>Testing OkHttp-based client implementations</li>
 *   <li>Comparing behavior across different HTTP client stacks</li>
 *   <li>Integration testing against containerized WordPress instances</li>
 * </ul>
 *
 * @see OkHttpWpRestClient
 * @see WpBasicAuthenticationStrategy
 * @see SslConfiguration
 */

public final class OkHttpWpBasicAuthRestClientFactory implements WpBasicAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public OkHttpWpBasicAuthRestClientFactory(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(final @NonNull String baseUrl,
                               final @NonNull String username,
                               final @NonNull String password) {
        return OkHttpWpRestClientBuilder.basicAuthentication(baseUrl, username, password)
                                        .withInterceptor(new OkHttpTestRequestInterceptor())
                                        .withInterceptor(new OkHttpTestResponseInterceptor())
                                        .withSslConfiguration(sslConfiguration)
                                        .build();
    }
}
