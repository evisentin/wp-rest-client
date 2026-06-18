package io.github.evisentin.wordpress.rest.client.adapter.okhttp;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpBasicAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.rest.client.domain.configuration.TimeoutConfiguration;
import lombok.NonNull;
import okhttp3.Interceptor;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Builder for creating {@link OkHttpWpRestClient} instances.
 *
 * <p>The builder supports configuration of SSL/TLS settings, request timeouts,
 * and custom OkHttp interceptors. Authentication is mandatory and must be provided through one of the available factory
 * methods.</p>
 *
 * <p>Example using HTTP Basic Authentication:</p>
 *
 * <pre>{@code
 * OkHttpWpRestClient client =
 *     OkHttpWpRestClientBuilder
 *         .basicAuthentication(
 *             "https://my-wordpress-site.com", // this must be the ROOT url, the client will find the API-URL via discovery
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
 * OkHttpWpRestClient client =
 *     OkHttpWpRestClientBuilder
 *         .jwtAuthentication(
 *             "https://my-wordpress-site.com", // this must be the ROOT url, the client will find the API-URL via discovery
 *             "user",
 *             "password",
 *             "/jwt-auth/v1/token" // ths must be relative to the API-URL
 *         )
 *         .build();
 * }</pre>
 */
public final class OkHttpWpRestClientBuilder {

    private final String baseUrl;
    private final WpAuthenticationStrategy authenticationStrategy;
    private final List<Interceptor> interceptors = new ArrayList<>();
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
    private OkHttpWpRestClientBuilder(final String baseUrl,
                                      final WpAuthenticationStrategy authenticationStrategy) {
        if (isBlank(baseUrl)) {
            throw new IllegalArgumentException("baseUrl cannot be blank");
        }

        this.baseUrl = baseUrl;
        this.authenticationStrategy = authenticationStrategy;
    }

    /**
     * Builds a configured {@link OkHttpWpRestClient}.
     *
     * @return A new REST client instance.
     */
    public OkHttpWpRestClient build() {
        return new OkHttpWpRestClient(
                baseUrl,
                authenticationStrategy,
                sslConfiguration,
                timeoutConfiguration,
                interceptors.toArray(Interceptor[]::new)
        );
    }

    /**
     * Adds a custom OkHttp interceptor.
     *
     * <p>Interceptors are executed in the order they are added.</p>
     *
     * @param interceptor
     *         Interceptor to add.
     *
     * @return This builder instance.
     */
    public OkHttpWpRestClientBuilder withInterceptor(final @NonNull Interceptor interceptor) {
        this.interceptors.add(interceptor);
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
    public OkHttpWpRestClientBuilder withSslConfiguration(final @NonNull SslConfiguration sslConfiguration) {
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
    public OkHttpWpRestClientBuilder withTimeoutConfiguration(final @NonNull TimeoutConfiguration timeoutConfiguration) {
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
    public static OkHttpWpRestClientBuilder basicAuthentication(final @NonNull String baseUrl,
                                                                final @NonNull String username,
                                                                final @NonNull String password) {
        return new OkHttpWpRestClientBuilder(baseUrl, new WpBasicAuthenticationStrategy(username, password));
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
     * @param jwtTokenEndPoint
     *         endpoint for the JWT token, relative to API-URL; must not be blank
     *
     * @return A new builder instance.
     */
    public static OkHttpWpRestClientBuilder jwtAuthentication(final @NonNull String baseUrl,
                                                              final @NonNull String username,
                                                              final @NonNull String password,
                                                              final @NonNull String jwtTokenEndPoint) {
        return new OkHttpWpRestClientBuilder(baseUrl, new WpJwtAuthenticationStrategy(username, password, jwtTokenEndPoint));
    }
}
