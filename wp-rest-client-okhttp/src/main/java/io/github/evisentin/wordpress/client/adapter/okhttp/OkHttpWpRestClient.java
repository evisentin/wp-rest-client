package io.github.evisentin.wordpress.client.adapter.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.discovery.ApiUrlDiscoveryHelper;
import io.github.evisentin.wordpress.client.adapter.okhttp.interceptors.AuthenticationInterceptor;
import io.github.evisentin.wordpress.client.adapter.okhttp.interceptors.WpErrorInterceptor;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.*;
import io.github.evisentin.wordpress.client.domain.api.WpBaseRestClient;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.client.domain.configuration.TimeoutConfiguration;
import io.github.evisentin.wordpress.client.domain.model.*;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.*;
import io.github.evisentin.wordpress.client.domain.model.requests.*;
import io.github.evisentin.wordpress.client.domain.model.responses.*;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
import static okhttp3.MultipartBody.FORM;
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

    private static final String API_URL = "apiUrl";
    private final String apiUrl;
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

        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        final OkHttpClient authHttpClient = buildAuthHttpClient(sslConfiguration, timeoutConfiguration);
        apiUrl = ApiUrlDiscoveryHelper.resolveApiUrl(authHttpClient, baseUrl);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationStrategy, authHttpClient, apiUrl))
                .addInterceptor(new WpErrorInterceptor());

        Arrays.stream(interceptors).forEach(clientBuilder::addInterceptor);

        applySslConfigurationIfPresent(clientBuilder, sslConfiguration);
        applyTimeoutConfigurationIfPresent(clientBuilder, timeoutConfiguration);

        this.httpClient = clientBuilder.build();
    }

    @Override
    @SneakyThrows
    public WpCategory createCategory(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment createComment(final @NonNull WpCommentCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getContent())) throw new IllegalArgumentException("content cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia createMedia(final @NonNull File file,
                               final @NonNull String fileName,
                               final @NonNull String mimeType) {

        if (isBlank(fileName)) throw new IllegalArgumentException("fileName cannot be blank");
        if (isBlank(mimeType)) throw new IllegalArgumentException("mimeType cannot be blank");

        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mimeType));

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));

        return performPostWithMultiPartBody(builder, requestBody, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost createPost(final @NonNull WpPostCreateUpdateRequest creationRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag createTag(final @NonNull WpTagCreateUpdateRequest creationRequest) {

        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse deleteCategory(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCommentDeletionResponse deleteComment(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMediaDeletionResponse deleteMedia(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostDeletionResponse deletePost(long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTagDeletionResponse deleteTag(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}",
                Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory getCategory(final long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment getComment(final long id, final WpContext context) {
        return getComment(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpComment getComment(final long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia getMedia(final long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    public WpPost getPost(final long id, final WpContext context) {
        return getPost(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpPost getPost(final long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostType getPostType(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/types/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_POST_TYPE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpPostType> getPostTypes() {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/types", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_POST_TYPES_MAP_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpStatus getStatus(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/statuses/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_STATUS_TYPE_REFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpStatus> getStatuses() {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/statuses", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_STATUSES_MAP_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag getTag(final long id, final WpContext context) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> listCategories(final @NonNull WpPagingQuery pageQuery,
                                                      final WpCategoryQuery categoryQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpComment> listComments(final @NonNull WpPagingQuery pageQuery,
                                                   final WpCommentQuery commentQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CommentQueryParamMapper.map(builder, commentQuery);

        return performPagingRequest(builder, pageQuery, WP_COMMENT_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpMedia> listMedia(final @NonNull WpPagingQuery pageQuery,
                                              final WpMediaQuery mediaQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        MediaQueryParamMapper.map(builder, mediaQuery);

        return performPagingRequest(builder, pageQuery, WP_MEDIA_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> listPosts(final @NonNull WpPagingQuery pageQuery, final WpPostQuery postQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpTag> listTags(final @NonNull WpPagingQuery pageQuery, final WpTagQuery tagQuery) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment trashComment(long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost trashPost(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}",
                Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory updateCategory(final long id,
                                     final @NonNull WpCategoryCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment updateComment(long id, final @NonNull WpCommentCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia updateMedia(final long id,
                               final @NonNull WpMediaUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost updatePost(final long id,
                             final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag updateTag(final long id,
                           final @NonNull WpTagCreateUpdateRequest updateRequest) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_TAG_TYPEREFERENCE);
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

    private <T> T performPostWithMultiPartBody(final HttpUrl.Builder urlBuilder,
                                               final MultipartBody requestBody,
                                               final TypeReference<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .post(requestBody)
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
