package io.github.evisentin.wordpress.rest.client.adapter.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.discovery.ApiUrlDiscoveryHelper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.interceptors.AuthenticationInterceptor;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.interceptors.WpErrorInterceptor;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.*;
import io.github.evisentin.wordpress.rest.client.domain.WpBaseRestClient;
import io.github.evisentin.wordpress.rest.client.domain.api.*;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.rest.client.domain.configuration.TimeoutConfiguration;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.Arrays;

import static org.apache.commons.lang3.ObjectUtils.anyNull;

/**
 * OkHttp-based implementation of {@link WpBaseRestClient}.
 *
 * <p>
 * This client provides a synchronous wrapper around the WordPress REST API using {@link okhttp3.OkHttpClient} as the
 * underlying HTTP transport.
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
 * to customize TLS behaviour and request timeouts.
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
public class OkHttpWpRestClient extends WpBaseRestClient {

    private final CategoryAPIs categoryAPIs;
    private final CommentAPIs commentAPIs;
    private final MediaAPIs mediaAPIs;
    private final PageAPIs pageAPIs;
    private final PostAPIs postAPIs;
    private final PostRevisionAPIs postRevisionAPIs;
    private final PostStatusAPIs postStatusAPIs;
    private final PostTypeAPIs postTypeAPIs;
    private final TagAPIs tagAPIs;
    private final TaxonomyAPIs taxonomyAPIs;

    /**
     * Creates a new {@code OkHttpWpRestClient}.
     *
     * <p>
     * The client is initialized with the given base URL and authentication strategy, and backed by an
     * {@link okhttp3.OkHttpClient} configured with internal authentication and error-handling interceptors.
     *
     * <p>
     * If a {@link SslConfiguration} is provided, it is applied to the underlying HTTP client. If {@code null}, the
     * default JVM SSL configuration is used.
     *
     * <p>
     * If a {@link TimeoutConfiguration} is provided, it is applied to the HTTP client. If {@code null}, OkHttp defaults
     * are used.
     *
     * @param baseUrl
     *         the base URL of the WordPress instance (must not be {@code null})
     * @param authenticationStrategy
     *         the authentication strategy used to sign requests (must not be {@code null})
     * @param sslConfiguration
     *         optional SSL configuration; may be {@code null}
     * @param timeoutConfiguration
     *         optional timeout configuration; may be {@code null}
     * @param interceptors
     *         additional OkHttp interceptors to register with the underlying HTTP client; may be empty
     *
     * @throws NullPointerException
     *         if {@code baseUrl} or {@code authenticationStrategy} is {@code null}
     * @throws IllegalStateException
     *         if the provided {@link SslConfiguration} is invalid
     */
    @SneakyThrows
    OkHttpWpRestClient(final @NonNull String baseUrl,
                       final @NonNull WpAuthenticationStrategy authenticationStrategy,
                       final SslConfiguration sslConfiguration,
                       final TimeoutConfiguration timeoutConfiguration,
                       final Interceptor... interceptors) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        final OkHttpClient authHttpClient = buildAuthHttpClient(sslConfiguration, timeoutConfiguration);
        String apiUrl = ApiUrlDiscoveryHelper.resolveApiUrl(authHttpClient, baseUrl);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationStrategy, authHttpClient, apiUrl))
                .addInterceptor(new WpErrorInterceptor());

        Arrays.stream(interceptors).forEach(clientBuilder::addInterceptor);

        applySslConfigurationIfPresent(clientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(clientBuilder, timeoutConfiguration);

        OkHttpClient httpClient = clientBuilder.build();

        categoryAPIs = new CategoryApiClientModule(apiUrl, httpClient, mapper);
        commentAPIs = new CommentApiClientModule(apiUrl, httpClient, mapper);
        mediaAPIs = new MediaApiClientModule(apiUrl, httpClient, mapper);
        pageAPIs = new PageApiClientModule(apiUrl, httpClient, mapper);
        postAPIs = new PostApiClientModule(apiUrl, httpClient, mapper);
        postRevisionAPIs = new PostRevisionApiClientModule(apiUrl, httpClient, mapper);
        postStatusAPIs = new PostStatusApiClientModule(apiUrl, httpClient, mapper);
        postTypeAPIs = new PostTypeApiClientModule(apiUrl, httpClient, mapper);
        tagAPIs = new TagApiClientModule(apiUrl, httpClient, mapper);
        taxonomyAPIs = new TaxonomyApiClientModule(apiUrl, httpClient, mapper);
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
        return mediaAPIs;
    }

    @Override
    public PageAPIs pages() {
        return pageAPIs;
    }

    @Override
    public PostRevisionAPIs postRevisions() {
        return postRevisionAPIs;
    }

    @Override
    public PostStatusAPIs postStatuses() {
        return postStatusAPIs;
    }

    @Override
    public PostTypeAPIs postTypes() {
        return postTypeAPIs;
    }

    @Override
    public PostAPIs posts() {
        return postAPIs;
    }

    @Override
    public TagAPIs tags() {
        return tagAPIs;
    }

    @Override
    public TaxonomyAPIs taxonomies() {
        return taxonomyAPIs;
    }

    private void applyTimeoutConfigurationIfPresent(final OkHttpClient.Builder clientBuilder,
                                                    final TimeoutConfiguration config) {
        if (config != null) {
            clientBuilder.connectTimeout(config.getConnectTimeout())
                         .readTimeout(config.getReadTimeout())
                         .writeTimeout(config.getWriteTimeout())
                         .callTimeout(config.getCallTimeout());
        }
    }

    private OkHttpClient buildAuthHttpClient(final SslConfiguration sslConfiguration,
                                             final TimeoutConfiguration timeoutConfiguration) {
        OkHttpClient.Builder authClientBuilder = new OkHttpClient.Builder();
        applySslConfigurationIfPresent(authClientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(authClientBuilder, timeoutConfiguration);

        return authClientBuilder.build();
    }

    private static void applySslConfigurationIfPresent(final OkHttpClient.Builder clientBuilder,
                                                       final SslConfiguration sslConfiguration) {
        if (sslConfiguration != null) {
            failOnInvalidConfiguration(sslConfiguration);
            clientBuilder.sslSocketFactory(sslConfiguration.getSslSocketFactory(), sslConfiguration.getTrustManager());

            if (sslConfiguration.getHostnameVerifier() != null) {
                clientBuilder.hostnameVerifier(sslConfiguration.getHostnameVerifier());
            }
        }
    }

    private static void failOnInvalidConfiguration(final SslConfiguration sslConfiguration) {
        if (anyNull(sslConfiguration.getSslSocketFactory(), sslConfiguration.getTrustManager())) {
            throw new IllegalStateException("SSL configuration requires both sslSocketFactory and trustManager");
        }
    }
}
