package io.github.evisentin.wordpress.client.adapter.apache;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.apache.discovery.ApiUrlDiscoveryHelper;
import io.github.evisentin.wordpress.client.adapter.apache.interceptors.AuthenticationInterceptor;
import io.github.evisentin.wordpress.client.adapter.apache.interceptors.WpErrorInterceptor;
import io.github.evisentin.wordpress.client.adapter.apache.modules.*;
import io.github.evisentin.wordpress.client.domain.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.api.*;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Apache HttpClient-based implementation of {@link WpBaseRestClient}.
 *
 * <p>
 * This client provides a synchronous wrapper around the WordPress REST API using
 * {@link org.apache.hc.client5.http.impl.classic.CloseableHttpClient} as the underlying HTTP transport.
 *
 * <p>
 * It supports CRUD operations and paginated queries for core WordPress resources such as posts, categories, and tags.
 *
 * <p>
 * The client is configured with an authentication strategy and automatically registers internal interceptors for:
 * <ul>
 *     <li>Authentication ({@link AuthenticationInterceptor})</li>
 *     <li>WordPress-specific error handling ({@link WpErrorInterceptor})</li>
 * </ul>
 *
 * <p>
 * Optional {@link SslConfiguration} and {@link TimeoutConfiguration} can be provided
 * to customize TLS behaviour, connection management, and request timeouts.
 *
 * <b>Thread Safety</b>
 *
 * <p>
 * Instances of this class are thread-safe and intended to be reused.
 *
 * @see WpBaseRestClient
 * @see AuthenticationInterceptor
 * @see WpErrorInterceptor
 */
public class ApacheWpRestClient extends WpBaseRestClient {

    private final CategoryAPIs categoryAPIs;
    private final CommentAPIs commentAPIs;
    private final MediaAPIs mediaPIs;
    private final PostAPIs postAPIs;
    private final PostStatusAPIs postStatusAPIs;
    private final PostTypeAPIs postTypesAPIs;
    private final TagAPIs tagAPIs;

    /**
     * Creates a new {@code ApacheWpRestClient}.
     *
     * <p>
     * The client is initialized with the given base URL and authentication strategy, and backed by an Apache
     * {@link CloseableHttpClient} configured with internal authentication and error-handling interceptors.
     *
     * <p>
     * If a {@link SslConfiguration} is provided, it is used to configure the underlying TLS strategy. If {@code null},
     * the default JVM SSL configuration is used.
     *
     * <p>
     * If a {@link TimeoutConfiguration} is provided, it is applied to the HTTP client, including connection, response,
     * and connection request timeouts. If {@code null}, default HttpClient settings are used.
     *
     * @param baseUrl
     *         the base URL of the WordPress instance (must not be {@code null})
     * @param authenticationStrategy
     *         the authentication strategy used to sign requests (must not be {@code null})
     * @param sslConfiguration
     *         optional SSL configuration; may be {@code null}
     * @param timeoutConfiguration
     *         optional timeout configuration; may be {@code null}
     * @param requestInterceptors
     *         additional request interceptors to register with the underlying HTTP client; may be {@code null} or
     *         empty
     * @param responseInterceptors
     *         additional response interceptors to register with the underlying HTTP client; may be {@code null} or
     *         empty
     *
     * @throws NullPointerException
     *         if {@code baseUrl} or {@code authenticationStrategy} is {@code null}
     * @throws IllegalStateException
     *         if the provided {@link SslConfiguration} is invalid
     */
    ApacheWpRestClient(final @NonNull String baseUrl,
                       final @NonNull WpAuthenticationStrategy authenticationStrategy,
                       final SslConfiguration sslConfiguration,
                       final TimeoutConfiguration timeoutConfiguration,
                       final List<HttpRequestInterceptor> requestInterceptors,
                       final List<HttpResponseInterceptor> responseInterceptors) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        final CloseableHttpClient authHttpClient = buildAuthHttpClient(sslConfiguration, timeoutConfiguration);

        String apiUrl = ApiUrlDiscoveryHelper.resolveApiUrl(authHttpClient, baseUrl);

        final HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.addRequestInterceptorFirst(new AuthenticationInterceptor(authenticationStrategy, authHttpClient, apiUrl));
        httpClientBuilder.addResponseInterceptorFirst(new WpErrorInterceptor());

        emptyIfNull(requestInterceptors).forEach(httpClientBuilder::addRequestInterceptorLast);
        emptyIfNull(responseInterceptors).forEach(httpClientBuilder::addResponseInterceptorLast);

        applySslConfigurationIfPresent(httpClientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(httpClientBuilder, timeoutConfiguration);

        CloseableHttpClient httpClient = httpClientBuilder.build();

        categoryAPIs = new CategoryApiClientModule(apiUrl, httpClient, mapper);
        commentAPIs = new CommentApiClientModule(apiUrl, httpClient, mapper);
        mediaPIs = new MediaApiClientModule(apiUrl, httpClient, mapper);
        postAPIs = new PostApiClientModule(apiUrl, httpClient, mapper);
        postStatusAPIs = new PostStatusApiClientModule(apiUrl, httpClient, mapper);
        postTypesAPIs = new PostTypeApiClientModule(apiUrl, httpClient, mapper);
        tagAPIs = new TagApiClientModule(apiUrl, httpClient, mapper);
    }

    @Override
    public CategoryAPIs categories() {
        return categoryAPIs;
    }

    @Override
    public CommentAPIs comments() {
        return commentAPIs;
    }

    @Override
    public MediaAPIs media() {
        return mediaPIs;
    }

    @Override
    public PostStatusAPIs postStatuses() {
        return postStatusAPIs;
    }

    @Override
    public PostTypeAPIs postTypes() {
        return postTypesAPIs;
    }

    @Override
    public PostAPIs posts() {
        return postAPIs;
    }

    @Override
    public TagAPIs tags() {
        return tagAPIs;
    }

    private static void applySslConfigurationIfPresent(final HttpClientBuilder httpClientBuilder,
                                                       final SslConfiguration sslConfiguration) {
        if (sslConfiguration != null) {
            failOnInvalidConfiguration(sslConfiguration);

            final SSLContext sslContext = createSslContext(sslConfiguration);

            final var tlsStrategyBuilder = ClientTlsStrategyBuilder.create()
                                                                   .setSslContext(sslContext);

            if (sslConfiguration.getHostnameVerifier() != null) {
                tlsStrategyBuilder.setHostnameVerifier(sslConfiguration.getHostnameVerifier());
            }

            final var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                                                                                   .setTlsSocketStrategy(tlsStrategyBuilder.buildClassic())
                                                                                   .build();

            httpClientBuilder.setConnectionManager(connectionManager);
        }
    }

    private static void applyTimeoutConfigurationIfPresent(final HttpClientBuilder httpClientBuilder,
                                                           final TimeoutConfiguration config) {
        if (config == null) {
            return;
        }

        final RequestConfig requestConfig = getRequestConfig(config);

        final PoolingHttpClientConnectionManager connectionManager = getPoolingHttpClientConnectionManager(config);

        httpClientBuilder
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig);
    }

    private static CloseableHttpClient buildAuthHttpClient(final SslConfiguration sslConfiguration,
                                                           final TimeoutConfiguration timeoutConfiguration) {
        final HttpClientBuilder authClientBuilder = HttpClients.custom();

        applySslConfigurationIfPresent(authClientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(authClientBuilder, timeoutConfiguration);

        return authClientBuilder.build();
    }

    @SneakyThrows
    private static SSLContext createSslContext(final SslConfiguration sslConfiguration) {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(
                null,
                new javax.net.ssl.TrustManager[]{sslConfiguration.getTrustManager()},
                new java.security.SecureRandom()
        );
        return sslContext;
    }

    private static <T> List<T> emptyIfNull(final List<T> list) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList);
    }

    private static void failOnInvalidConfiguration(final SslConfiguration sslConfiguration) {
        if (sslConfiguration.getTrustManager() == null) {
            throw new IllegalStateException("SSL configuration requires a trustManager");
        }
    }

    private static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(final TimeoutConfiguration config) {
        ConnectionConfig.Builder connBuilder = ConnectionConfig.custom();

        Optional.ofNullable(config.getConnectTimeout())
                .map(Timeout::of)
                .ifPresent(connBuilder::setConnectTimeout);

        ConnectionConfig connectionConfig = connBuilder.build();

        return PoolingHttpClientConnectionManagerBuilder.create()
                                                        .setDefaultConnectionConfig(connectionConfig)
                                                        .build();
    }

    private static RequestConfig getRequestConfig(final TimeoutConfiguration config) {
        RequestConfig.Builder builder = RequestConfig.custom();

        Optional.ofNullable(config.getReadTimeout())
                .map(Timeout::of)
                .ifPresent(builder::setResponseTimeout);

        Optional.ofNullable(config.getConnectionRequestTimeout())
                .map(Timeout::of)
                .ifPresent(builder::setConnectionRequestTimeout);

        return builder.build();
    }
}
