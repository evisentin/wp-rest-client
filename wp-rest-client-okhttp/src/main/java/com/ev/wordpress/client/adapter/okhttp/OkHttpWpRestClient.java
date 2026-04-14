package com.ev.wordpress.client.adapter.okhttp;

import com.ev.wordpress.client.adapter.okhttp.interceptors.AuthenticationInterceptor;
import com.ev.wordpress.client.adapter.okhttp.interceptors.WpErrorInterceptor;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.CategoryQueryParamMapper;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.PostQueryParamMapper;
import com.ev.wordpress.client.adapter.okhttp.query.mappers.TagQueryParamMapper;
import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class OkHttpWpRestClient extends WpBaseRestClient {

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    @SneakyThrows
    public OkHttpWpRestClient(final @NonNull String baseUrl,
                              final @NonNull WpAuthenticationStrategy authenticationStrategy) {
        super(baseUrl, authenticationStrategy);
        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        X509TrustManager trustAll = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustAll}, new SecureRandom());

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        this.client =
                new OkHttpClient.Builder()
                        .addInterceptor(new AuthenticationInterceptor(authenticationStrategy))
                        .addInterceptor(new WpErrorInterceptor())
                        .sslSocketFactory(sslSocketFactory, trustAll)
                        .hostnameVerifier((hostname, session) -> true)
                        .build();
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

    private <T> T performDeleteRequest(final HttpUrl.Builder urlBuilder,
                                       final TypeReference<T> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
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

        try (Response response = client.newCall(request).execute()) {
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

        try (Response response = client.newCall(request).execute()) {
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

        try (Response response = client.newCall(request).execute()) {
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

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }
}
