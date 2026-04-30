package com.ev.wordpress.test.integration.base.factory;

import com.ev.wordpress.client.adapter.okhttp.OkHttpWpRestClient;
import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;

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

    public OkHttpWpBasicAuthRestClientFactory(SslConfiguration sslConfiguration) {
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
