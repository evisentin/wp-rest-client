package com.ev.wordpress.client.adapter.apache;

import com.ev.wordpress.client.adapter.apache.interceptors.AuthenticationInterceptor;
import com.ev.wordpress.client.adapter.apache.interceptors.WpErrorInterceptor;
import com.ev.wordpress.client.adapter.apache.query.mappers.CategoryQueryParamMapper;
import com.ev.wordpress.client.adapter.apache.query.mappers.PostQueryParamMapper;
import com.ev.wordpress.client.adapter.apache.query.mappers.TagQueryParamMapper;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.text.StringSubstitutor;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.ev.wordpress.client.adapter.apache.TypeReferences.*;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.CONTEXT;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.FORCE;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.PAGE;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ApacheWpRestClient extends WpBaseRestClient {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;

    public ApacheWpRestClient(String baseUrl, WpAuthenticationStrategy authenticationStrategy) {
        super(baseUrl, authenticationStrategy);
        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        this.httpClient = HttpClients.custom()
                                     .addRequestInterceptorFirst(new AuthenticationInterceptor(authenticationStrategy))
                                     .addResponseInterceptorFirst(new WpErrorInterceptor())
                                     // add interceptors, timeouts, etc.
                                     .build();
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory createCategory(@NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost createPost(@NonNull WpPostCreateUpdateRequest creationRequest) {

        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag createTag(@NonNull WpTagCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags",
                Map.of("baseUrl", baseUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategoryDeletionResponse deleteCategory(@NonNull Long id) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPostDeletionResponse deletePost(@NonNull Long id) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTagDeletionResponse deleteTag(@NonNull Long id) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory getCategory(@NonNull Long id, WpContext context) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost getPost(@NonNull Long id, WpContext context) {
        return getPost(id, context, null);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost getPost(final @NonNull Long id, final WpContext context, final String password) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag getTag(final @NonNull Long id, final WpContext context) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPagedResponse<WpCategory> listCategories(@NonNull WpPagingQuery pageQuery, WpCategoryQuery categoryQuery) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories",
                Map.of("baseUrl", baseUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPagedResponse<WpPost> listPosts(@NonNull WpPagingQuery pageQuery, WpPostQuery postQuery) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts",
                Map.of("baseUrl", baseUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPagedResponse<WpTag> listTags(@NonNull WpPagingQuery pageQuery, WpTagQuery tagQuery) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags",
                Map.of("baseUrl", baseUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost trashPost(@NonNull Long id) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        builder.addParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory updateCategory(@NonNull Long id, @NonNull WpCategoryCreateUpdateRequest updateRequest) {

        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/categories/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost updatePost(@NonNull Long id, @NonNull WpPostCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/posts/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag updateTag(@NonNull Long id, @NonNull WpTagCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${baseUrl}/wp-json/wp/v2/tags/${id}",
                Map.of("baseUrl", baseUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_TAG_TYPE);
    }

    @SneakyThrows
    private <T> T performDeleteRequest(final URIBuilder uriBuilder,
                                       final TypeReference<T> responseType) throws IOException {

        URI uri = uriBuilder.build();

        HttpDelete request = new HttpDelete(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    private <T> T performGetRequest(final URIBuilder uriBuilder,
                                    final TypeReference<T> responseType) throws IOException {

        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    private <T> WpPagedResponse<T> performPagingRequest(final URIBuilder uriBuilder,
                                                        final WpPagingQuery pageQuery,
                                                        final TypeReference<List<T>> responseType) throws IOException {
        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            int totalItems = ofNullable(response.getHeader("X-WP-Total"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            int totalPages = ofNullable(response.getHeader("X-WP-TotalPages"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            if (response.getEntity() == null) {
                throw new IOException("Empty response body");
            }

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
                                      final TypeReference<T> responseType) throws IOException {

        URI uri = uriBuilder.build();

        HttpPost request = new HttpPost(uri);
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String jsonBody = mapper.writeValueAsString(requestBody);
        request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

        return httpClient.execute(request, response -> {
            if (response.getEntity() == null) {
                throw new IOException("Empty response body");
            }

            String json = EntityUtils.toString(response.getEntity());
            return mapper.readValue(json, responseType);
        });
    }

    @SneakyThrows
    private URIBuilder urlBuilder(final String path, final Map<String, Object> pathParams) {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return new URIBuilder(substituted);
    }

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }
}
