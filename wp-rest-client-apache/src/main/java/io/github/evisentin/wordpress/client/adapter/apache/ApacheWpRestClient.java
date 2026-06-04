package io.github.evisentin.wordpress.client.adapter.apache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.apache.discovery.ApiUrlDiscoveryHelper;
import io.github.evisentin.wordpress.client.adapter.apache.interceptors.AuthenticationInterceptor;
import io.github.evisentin.wordpress.client.adapter.apache.interceptors.WpErrorInterceptor;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.CategoryQueryParamMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.MediaQueryParamMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.PostQueryParamMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.TagQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import io.github.evisentin.wordpress.client.domain.model.*;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.*;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpMediaUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpMediaDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.text.StringSubstitutor;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.evisentin.wordpress.client.adapter.apache.TypeReferences.*;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.hc.core5.http.HttpHeaders.ACCEPT;

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

    private static final String API_URL = "apiUrl";
    private final String apiUrl;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;

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

        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        final CloseableHttpClient authHttpClient = buildAuthHttpClient(sslConfiguration, timeoutConfiguration);

        apiUrl = ApiUrlDiscoveryHelper.resolveApiUrl(authHttpClient, baseUrl);

        final HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.addRequestInterceptorFirst(new AuthenticationInterceptor(authenticationStrategy, authHttpClient, apiUrl));
        httpClientBuilder.addResponseInterceptorFirst(new WpErrorInterceptor());

        emptyIfNull(requestInterceptors).forEach(httpClientBuilder::addRequestInterceptorLast);
        emptyIfNull(responseInterceptors).forEach(httpClientBuilder::addResponseInterceptorLast);

        applySslConfigurationIfPresent(httpClientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(httpClientBuilder, timeoutConfiguration);

        this.httpClient = httpClientBuilder.build();
    }

    @Override
    @SneakyThrows
    public WpCategory createCategory(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia createMedia(final @NonNull File file,
                               final @NonNull String fileName,
                               final @NonNull String mimeType) {

        if (isBlank(fileName)) throw new IllegalArgumentException("fileName cannot be blank");
        if (isBlank(mimeType)) throw new IllegalArgumentException("mimeType cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));
        return performPostWithFileUpload(builder, file, fileName, mimeType, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost createPost(final @NonNull WpPostCreateUpdateRequest creationRequest) {

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag createTag(final @NonNull WpTagCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse deleteCategory(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMediaDeletionResponse deleteMedia(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostDeletionResponse deletePost(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTagDeletionResponse deleteTag(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory getCategory(final long id, final WpContext context) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia getMedia(final long id, final WpContext context) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost getPost(final long id, final WpContext context) {
        return getPost(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpPost getPost(final long id, final WpContext context, final String password) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostType getPostType(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/types/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_POST_TYPE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpPostType> getPostTypes() {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/types", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_POST_TYPES_MAP_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpStatus getStatus(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/statuses/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_STATUS_TYPE_REFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpStatus> getStatuses() {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/statuses", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_STATUSES_MAP_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag getTag(final long id, final WpContext context) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> listCategories(final @NonNull WpPagingQuery pageQuery,
                                                      final WpCategoryQuery categoryQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpMedia> listMedia(final @NonNull WpPagingQuery pageQuery,
                                              final WpMediaQuery mediaQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        MediaQueryParamMapper.map(builder, mediaQuery);

        return performPagingRequest(builder, pageQuery, WP_MEDIA_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> listPosts(final @NonNull WpPagingQuery pageQuery,
                                             final WpPostQuery postQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpTag> listTags(final @NonNull WpPagingQuery pageQuery,
                                           final WpTagQuery tagQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost trashPost(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory updateCategory(final long id,
                                     final @NonNull WpCategoryCreateUpdateRequest updateRequest) {

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia updateMedia(final long id,
                               final @NonNull WpMediaUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost updatePost(final long id,
                             final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag updateTag(final long id,
                           final @NonNull WpTagCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_TAG_TYPEREFERENCE);
    }

    @SneakyThrows
    private <T> T performDeleteRequest(final URIBuilder uriBuilder,
                                       final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpDelete request = new HttpDelete(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    private <T> T performGetRequest(final URIBuilder uriBuilder,
                                    final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    private <T> WpPagedResponse<T> performPagingRequest(final URIBuilder uriBuilder,
                                                        final WpPagingQuery pageQuery,
                                                        final TypeReference<List<T>> responseType) {
        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            int totalItems = ofNullable(response.getHeader("X-WP-Total"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            int totalPages = ofNullable(response.getHeader("X-WP-TotalPages"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            List<T> items = mapper.readValue(json, responseType);

            return new WpPagedResponse<>(
                    items,
                    pageQuery.getPageSize(),
                    totalItems,
                    totalPages,
                    pageQuery.getPageNumber()
            );
        });
    }

    @SneakyThrows
    private <T> T performPostWithBody(final URIBuilder uriBuilder,
                                      final Object requestBody,
                                      final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpPost request = new HttpPost(uri);
        request.setHeader(ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String jsonBody = mapper.writeValueAsString(requestBody);
        request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

        return httpClient.execute(request, response -> {
            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            return mapper.readValue(json, responseType);
        });
    }

    @SneakyThrows
    private <T> T performPostWithFileUpload(final URIBuilder uriBuilder,
                                            final File file,
                                            final String fileName,
                                            final String mimeType,
                                            final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpPost request = new HttpPost(uri);
        request.setHeader(ACCEPT, "application/json");

        request.setEntity(
                MultipartEntityBuilder.create()
                                      .addBinaryBody(
                                              "file",
                                              file,
                                              ContentType.parse(mimeType),
                                              fileName
                                      )

                                      .build()
        );

        return httpClient.execute(request, response -> {
            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            return mapper.readValue(json, responseType);
        });
    }

    private URIBuilder urlBuilder(final String path, final Map<String, Object> pathParams) throws URISyntaxException {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return new URIBuilder(substituted);
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

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }

    private static <T> List<T> emptyIfNull(final List<T> list) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList);
    }

    private static void failOnEmptyResponseBody(final ClassicHttpResponse response) throws IOException {
        if (response.getEntity() == null) {
            throw new IOException("Empty response body");
        }
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
