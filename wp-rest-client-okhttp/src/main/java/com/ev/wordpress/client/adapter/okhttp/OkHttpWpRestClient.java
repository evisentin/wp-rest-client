package com.ev.wordpress.client.adapter.okhttp;

import com.ev.wordpress.client.adapter.okhttp.interceptors.AuthenticationInterceptor;
import com.ev.wordpress.client.adapter.okhttp.interceptors.WpErrorInterceptor;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.CategoryQueryParamMapper;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.PostQueryParamMapper;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.TagQueryParamMapper;
import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import com.ev.wordpress.client.domain.configuration.SslConfiguration;
import com.ev.wordpress.client.domain.configuration.TimeoutConfiguration;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.WpPagedResponse;
import com.ev.wordpress.client.domain.dto.WpPost;
import com.ev.wordpress.client.domain.dto.WpTag;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.query.WpCategoryQuery;
import com.ev.wordpress.client.domain.dto.query.WpPagingQuery;
import com.ev.wordpress.client.domain.dto.query.WpPostQuery;
import com.ev.wordpress.client.domain.dto.query.WpTagQuery;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.requests.WpPostCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.requests.WpTagCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.client.domain.dto.responses.WpPostDeletionResponse;
import com.ev.wordpress.client.domain.dto.responses.WpTagDeletionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.ev.wordpress.client.adapter.okhttp.TypeReferences.*;
import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.ACCEPT;
import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.X_WP_TOTAL;
import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.X_WP_TOTAL_PAGES;
import static com.ev.wordpress.client.adapter.okhttp.constants.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static com.ev.wordpress.client.adapter.okhttp.constants.MimeTypes.APPLICATION_JSON;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.CONTEXT;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.FORCE;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.PAGE;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.anyNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class OkHttpWpRestClient extends WpBaseRestClient {

    private final OkHttpClient httpClient;
    private final ObjectMapper mapper;

    /**
     * Creates a new {@code OkHttpWpRestClient} instance.
     *
     * <p>
     * The client is configured with the provided base URL and authentication strategy, and uses
     * {@link okhttp3.OkHttpClient} as the underlying HTTP transport.
     *
     * <p>
     * If a {@link SslConfiguration} is provided, its settings will be applied to the HTTP client, allowing
     * customization of SSL/TLS behaviour (e.g. custom trust store, mutual TLS, or hostname verification). If
     * {@code null}, the default JVM SSL configuration is used.
     *
     * <p>
     * The client automatically registers internal interceptors for:
     * <ul>
     *     <li>Authentication handling</li>
     *     <li>WordPress-specific error handling</li>
     * </ul>
     *
     * <h4>Usage examples</h4>
     *
     * <p><b>Default (secure) configuration:</b></p>
     * <pre>{@code
     * OkHttpWpRestClient client = new OkHttpWpRestClient(
     *     "https://example.com",
     *     authenticationStrategy,
     *     null
     * );
     * }</pre>
     *
     * <p><b>Custom SSL configuration:</b></p>
     * <pre>{@code
     * SSLContext sslContext = SSLContext.getInstance("TLS");
     * sslContext.init(null, new TrustManager[]{trustManager}, null);
     *
     * SslConfiguration sslConfig = SslConfiguration.builder()
     *     .sslSocketFactory(sslContext.getSocketFactory())
     *     .trustManager(trustManager)
     *     .hostnameVerifier((hostname, session) -> true) // optional, ⚠ disables hostname verification, DO NOT USE IN PRODUCTION
     *     .build();
     *
     * OkHttpWpRestClient client = new OkHttpWpRestClient(
     *     "https://example.com",
     *     authenticationStrategy,
     *     sslConfig
     * );
     * }</pre>
     *
     * @param baseUrl
     *         the base URL of the WordPress instance (must not be {@code null})
     * @param authenticationStrategy
     *         the authentication strategy to use for requests (must not be {@code null})
     * @param sslConfiguration
     *         optional SSL configuration; if {@code null}, the default SSL behaviour is used
     *
     * @throws NullPointerException
     *         if {@code baseUrl} or {@code authenticationStrategy} is {@code null}
     * @throws IllegalStateException
     *         if the provided {@link SslConfiguration} is incomplete (e.g. missing required SSL components)
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
                .addInterceptor(new AuthenticationInterceptor(authenticationStrategy))
                .addInterceptor(new WpErrorInterceptor());

        applySslConfigurationIfPresent(clientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(clientBuilder, timeoutConfiguration);

        this.httpClient = clientBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpCategory createCategory(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPost createPost(final @NonNull WpPostCreateUpdateRequest creationRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpTag createTag(final @NonNull WpTagCreateUpdateRequest creationRequest) {

        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse deleteCategory(final @NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPostDeletionResponse deletePost(@NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpTagDeletionResponse deleteTag(final @NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory getCategory(final @NonNull Long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WpPost getPost(final @NonNull Long id, final WpContext context) {
        return getPost(id, context, null);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost getPost(final @NonNull Long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag getTag(@NonNull final Long id, final WpContext context) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> listCategories(final @NonNull WpPagingQuery pageQuery,
                                                      final WpCategoryQuery categoryQuery) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories", Map.of("baseUrl", baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> listPosts(final @NonNull WpPagingQuery pageQuery, final WpPostQuery postQuery) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts", Map.of("baseUrl", baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPagedResponse<WpTag> listTags(final @NonNull WpPagingQuery pageQuery, final WpTagQuery tagQuery) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags", Map.of("baseUrl", baseUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPost trashPost(@NonNull Long id) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpCategory updateCategory(final @NonNull Long id,
                                     final @NonNull WpCategoryCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl,
                        "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpPost updatePost(final @NonNull Long id,
                             final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl,
                        "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SneakyThrows
    public WpTag updateTag(final @NonNull Long id,
                           final @NonNull WpTagCreateUpdateRequest updateRequest) {

        final HttpUrl.Builder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl,
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
