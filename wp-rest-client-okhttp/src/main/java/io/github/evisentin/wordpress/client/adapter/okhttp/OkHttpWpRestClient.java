package io.github.evisentin.wordpress.client.adapter.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.interceptors.AuthenticationInterceptor;
import io.github.evisentin.wordpress.client.adapter.okhttp.interceptors.LoggingInterceptor;
import io.github.evisentin.wordpress.client.adapter.okhttp.interceptors.WpErrorInterceptor;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.CategoryQueryParamMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.PostQueryParamMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.TagQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.WpTag;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.evisentin.wordpress.client.adapter.okhttp.TypeReferences.*;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpHeaders.ACCEPT;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpHeaders.X_WP_TOTAL;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpHeaders.X_WP_TOTAL_PAGES;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static io.github.evisentin.wordpress.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.anyNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

    public static final String BASE_URL = "baseUrl";
    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

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
     *
     * @throws NullPointerException
     *         if {@code baseUrl} or {@code authenticationStrategy} is {@code null}
     * @throws IllegalStateException
     *         if the provided {@link SslConfiguration} is invalid
     */
    @SneakyThrows
    public OkHttpWpRestClient(final @NonNull String baseUrl,
                              final @NonNull WpAuthenticationStrategy authenticationStrategy,
                              final SslConfiguration sslConfiguration,
                              final TimeoutConfiguration timeoutConfiguration) {
        super(baseUrl, authenticationStrategy);
        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationStrategy, buildAuthHttpClient(sslConfiguration, timeoutConfiguration)))
                .addInterceptor(new WpErrorInterceptor())
                .addInterceptor(new LoggingInterceptor());

        applySslConfigurationIfPresent(clientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(clientBuilder, timeoutConfiguration);

        this.httpClient = clientBuilder.build();
    }

    @Override
    @SneakyThrows
    public WpCategory createCategory(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories",
                Map.of(BASE_URL, baseUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPost createPost(final @NonNull WpPostCreateUpdateRequest creationRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts",
                Map.of(BASE_URL, baseUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpTag createTag(final @NonNull WpTagCreateUpdateRequest creationRequest) {

        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags",
                Map.of(BASE_URL, baseUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPE);
    }

    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse deleteCategory(final @NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPostDeletionResponse deletePost(@NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPE);
    }

    @Override
    @SneakyThrows
    public WpTagDeletionResponse deleteTag(final @NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPE);
    }

    @SneakyThrows
    @Override
    public WpCategory getCategory(final @NonNull Long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPE);
    }

    @Override
    public WpPost getPost(final @NonNull Long id, final WpContext context) {
        return getPost(id, context, null);
    }

    @SneakyThrows
    @Override
    public WpPost getPost(final @NonNull Long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPE);
    }

    @SneakyThrows
    @Override
    public WpTag getTag(@NonNull final Long id, final WpContext context) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> listCategories(final @NonNull WpPagingQuery pageQuery,
                                                      final WpCategoryQuery categoryQuery) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories", Map.of(BASE_URL, baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> listPosts(final @NonNull WpPagingQuery pageQuery, final WpPostQuery postQuery) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts", Map.of(BASE_URL, baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpTag> listTags(final @NonNull WpPagingQuery pageQuery, final WpTagQuery tagQuery) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags", Map.of(BASE_URL, baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPost trashPost(@NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of(BASE_URL, baseUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpCategory updateCategory(final @NonNull Long id,
                                     final @NonNull WpCategoryCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of(BASE_URL, baseUrl,
                        "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPE);
    }

    @Override
    @SneakyThrows
    public WpPost updatePost(final @NonNull Long id,
                             final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of(BASE_URL, baseUrl,
                        "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPE);
    }

    @Override
    @SneakyThrows
    public WpTag updateTag(final @NonNull Long id,
                           final @NonNull WpTagCreateUpdateRequest updateRequest) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of(BASE_URL, baseUrl,
                        "id", id));

        return performPostWithBody(builder, updateRequest, WP_TAG_TYPE);
    }

    private void applyTimeoutConfigurationIfPresent(OkHttpClient.Builder clientBuilder, TimeoutConfiguration config) {
        if (config != null) {
            clientBuilder.connectTimeout(config.getConnectTimeout())
                         .readTimeout(config.getReadTimeout())
                         .writeTimeout(config.getWriteTimeout())
                         .callTimeout(config.getCallTimeout());
        }
    }

    private OkHttpClient buildAuthHttpClient(SslConfiguration sslConfiguration, TimeoutConfiguration timeoutConfiguration) {
        OkHttpClient.Builder authClientBuilder = new OkHttpClient.Builder();
        applySslConfigurationIfPresent(authClientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(authClientBuilder, timeoutConfiguration);

        return authClientBuilder.build();
    }

    private <T> T performDeleteRequest(final HttpUrl.Builder urlBuilder,
                                       final TypeReference<T> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final ResponseBody body = response.body();

            return mapper.readValue(body.string(), responseType);
        }
    }

    private <T> T performGetRequest(final HttpUrl.Builder urlBuilder,
                                    final TypeReference<T> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final ResponseBody body = response.body();

            return mapper.readValue(body.string(), responseType);
        }
    }

    private <T> WpPagedResponse<T> performPagingRequest(final HttpUrl.Builder urlBuilder,
                                                        final WpPagingQuery pageQuery,
                                                        final TypeReference<List<T>> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final int totalItems = Integer.parseInt(requireNonNull(response.header(X_WP_TOTAL, "0")));
            final int totalPages = Integer.parseInt(requireNonNull(response.header(X_WP_TOTAL_PAGES, "0")));
            final String json = response.body().string();

            final List<T> items = mapper.readValue(json, responseType);

            return new WpPagedResponse<>(
                    items,
                    pageQuery.getPageSize(),
                    totalItems,
                    totalPages,
                    pageQuery.getPageNumber()
            );
        }
    }

    private <T> T performPostWithBody(final HttpUrl.Builder urlBuilder,
                                      final Object requestBody,
                                      final TypeReference<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .post(RequestBody.create(toJson(requestBody), MEDIA_TYPE_APPLICATION_JSON))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            val json = response.body().string();
            return mapper.readValue(json, responseType);
        }
    }

    private String toJson(final Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    private HttpUrl.Builder urlBuilder(final String path, final Map<String, Object> pathParams) {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return Objects.requireNonNull(HttpUrl.parse(substituted)).newBuilder();
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

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }

    private static void failOnInvalidConfiguration(final SslConfiguration sslConfiguration) {
        if (anyNull(sslConfiguration.getSslSocketFactory(), sslConfiguration.getTrustManager())) {
            throw new IllegalStateException("SSL configuration requires both sslSocketFactory and trustManager");
        }
    }
}
