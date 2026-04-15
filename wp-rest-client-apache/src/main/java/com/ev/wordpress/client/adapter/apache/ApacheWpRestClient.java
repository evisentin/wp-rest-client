package com.ev.wordpress.client.adapter.apache;

import com.ev.wordpress.client.adapter.apache.interceptors.AuthenticationInterceptor;
import com.ev.wordpress.client.adapter.apache.interceptors.WpErrorInterceptor;
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
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static com.ev.wordpress.client.adapter.apache.TypeReferences.WP_CATEGORY_TYPE;
import static com.ev.wordpress.client.adapter.apache.TypeReferences.WP_POST_TYPE;
import static com.ev.wordpress.client.adapter.apache.TypeReferences.WP_TAG_TYPE;
import static com.ev.wordpress.client.domain.dto.parameters.WpQueryParameters.CONTEXT;
import static java.util.Optional.ofNullable;
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
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost createPost(@NonNull WpPostCreateUpdateRequest creationRequest) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag createTag(@NonNull WpTagCreateUpdateRequest creationRequest) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategoryDeletionResponse deleteCategory(@NonNull Long id) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPostDeletionResponse deletePost(@NonNull Long id) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTagDeletionResponse deleteTag(@NonNull Long id) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
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
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPagedResponse<WpPost> listPosts(@NonNull WpPagingQuery pageQuery, WpPostQuery postQuery) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPagedResponse<WpTag> listTags(@NonNull WpPagingQuery pageQuery, WpTagQuery tagQuery) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost trashPost(@NonNull Long id) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory updateCategory(@NonNull Long id, @NonNull WpCategoryCreateUpdateRequest updateRequest) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost updatePost(@NonNull Long id, @NonNull WpPostCreateUpdateRequest updateRequest) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag updateTag(@NonNull Long id, @NonNull WpTagCreateUpdateRequest updateRequest) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
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
    private URIBuilder urlBuilder(final String path, final Map<String, Object> pathParams) {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return new URIBuilder(substituted);
    }

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }
}
