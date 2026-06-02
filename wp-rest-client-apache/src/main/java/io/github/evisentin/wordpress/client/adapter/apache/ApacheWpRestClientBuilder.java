package io.github.evisentin.wordpress.client.adapter.apache;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import lombok.NonNull;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponseInterceptor;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Builder for creating {@link ApacheWpRestClient} instances.
 *
 * <p>The builder supports configuration of SSL/TLS settings, request timeouts, and custom Apache HttpClient request
 * and response interceptors. Authentication is mandatory and must be provided through one of the available factory
 * methods.</p>
 *
 * <p>Example using HTTP Basic Authentication:</p>
 *
 * <pre>{@code
 * ApacheWpRestClient client =
 *     ApacheWpRestClientBuilder
 *         .basicAuthentication(
 *             "https://my-wordpress-site.com",
 *             "user",
 *             "password"
 *         )
 *         .withTimeoutConfiguration(timeoutConfiguration)
 *         .build();
 * }</pre>
 *
 * <p>Example using JWT Authentication:</p>
 *
 * <pre>{@code
 * ApacheWpRestClient client =
 *     ApacheWpRestClientBuilder
 *         .jwtAuthentication(
 *             "https://my-wordpress-site.com",
 *             "user",
 *             "password",
 *             "https://my-wordpress-site.com/wp-json/jwt-auth/v1/token"
 *         )
 *         .build();
 * }</pre>
 */
public final class ApacheWpRestClientBuilder {

    private final String baseUrl;
    private final WpAuthenticationStrategy authenticationStrategy;
    private final List<HttpRequestInterceptor> requestInterceptors = new ArrayList<>();
    private final List<HttpResponseInterceptor> responseInterceptors = new ArrayList<>();
    private SslConfiguration sslConfiguration;
    private TimeoutConfiguration timeoutConfiguration;

    /**
     * Creates a builder configured for the supplied authentication strategy.
     *
     * @param baseUrl
     *         Base URL of the WordPress instance.
     * @param authenticationStrategy
     *         Authentication strategy used by the client.
     *
     * @throws IllegalArgumentException
     *         if {@code baseUrl} is blank.
     */
    private ApacheWpRestClientBuilder(final String baseUrl,
                                      final WpAuthenticationStrategy authenticationStrategy) {
        if (isBlank(baseUrl)) {
            throw new IllegalArgumentException("baseUrl cannot be blank");
        }

        this.baseUrl = baseUrl;
        this.authenticationStrategy = authenticationStrategy;
    }

    /**
     * Builds a configured {@link ApacheWpRestClient}.
     *
     * @return A new REST client instance.
     */
    public ApacheWpRestClient build() {
        return new ApacheWpRestClient(
                baseUrl,
                authenticationStrategy,
                sslConfiguration,
                timeoutConfiguration,
                requestInterceptors,
                responseInterceptors
        );
    }

    /**
     * Adds a custom Apache HttpClient request interceptor.
     *
     * <p>Request interceptors are executed in the order they are added
     * before a request is sent.</p>
     *
     * @param interceptor
     *         Request interceptor to add.
     *
     * @return This builder instance.
     */
    public ApacheWpRestClientBuilder withInterceptor(final @NonNull HttpRequestInterceptor interceptor) {
        this.requestInterceptors.add(interceptor);
        return this;
    }

    /**
     * Adds a custom Apache HttpClient response interceptor.
     *
     * <p>Response interceptors are executed in the order they are added
     * after a response is received.</p>
     *
     * @param interceptor
     *         Response interceptor to add.
     *
     * @return This builder instance.
     */
    public ApacheWpRestClientBuilder withInterceptor(final @NonNull HttpResponseInterceptor interceptor) {
        this.responseInterceptors.add(interceptor);
        return this;
    }

    /**
     * Configures SSL/TLS settings for the client.
     *
     * @param sslConfiguration
     *         SSL configuration to use.
     *
     * @return This builder instance.
     */
    public ApacheWpRestClientBuilder withSslConfiguration(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
        return this;
    }

    /**
     * Configures request timeout settings for the client.
     *
     * @param timeoutConfiguration
     *         Timeout configuration to use.
     *
     * @return This builder instance.
     */
    public ApacheWpRestClientBuilder withTimeoutConfiguration(final @NonNull TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
        return this;
    }

    /**
     * Creates a builder configured for HTTP Basic Authentication.
     *
     * @param baseUrl
     *         Base URL of the WordPress instance.
     * @param username
     *         Username used for authentication.
     * @param password
     *         Password used for authentication.
     *
     * @return A new builder instance.
     */
    public static ApacheWpRestClientBuilder basicAuthentication(final @NonNull String baseUrl,
                                                                final @NonNull String username,
                                                                final @NonNull String password) {
        return new ApacheWpRestClientBuilder(baseUrl, new WpBasicAuthenticationStrategy(username, password));
    }

    /**
     * Creates a builder configured for JWT-based authentication.
     *
     * <p>The client obtains JWT tokens from the supplied token endpoint
     * using the provided credentials.</p>
     *
     * @param baseUrl
     *         Base URL of the WordPress instance.
     * @param username
     *         Username used to obtain JWT tokens.
     * @param password
     *         Password used to obtain JWT tokens.
     * @param jwtTokenUrl
     *         Endpoint used to retrieve JWT tokens.
     *
     * @return A new builder instance.
     */
    public static ApacheWpRestClientBuilder jwtAuthentication(final @NonNull String baseUrl,
                                                              final @NonNull String username,
                                                              final @NonNull String password,
                                                              final @NonNull String jwtTokenUrl) {
        return new ApacheWpRestClientBuilder(baseUrl, new WpJwtAuthenticationStrategy(username, password, jwtTokenUrl));
    }
}
