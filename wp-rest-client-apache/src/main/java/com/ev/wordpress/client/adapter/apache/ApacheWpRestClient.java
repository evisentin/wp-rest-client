package com.ev.wordpress.client.adapter.apache;

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
import lombok.NonNull;
import lombok.SneakyThrows;

public class ApacheWpRestClient extends WpBaseRestClient {

    public ApacheWpRestClient(String baseUrl, WpAuthenticationStrategy authenticationStrategy) {
        super(baseUrl, authenticationStrategy);
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
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPostDeletionResponse deletePost(@NonNull Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTagDeletionResponse deleteTag(@NonNull Long id) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpCategory getCategory(@NonNull Long id, WpContext context) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost getPost(@NonNull Long id, WpContext context) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpPost getPost(@NonNull Long id, WpContext context, String password) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public WpTag getTag(final @NonNull Long id, final WpContext context) {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
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
        return null;
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
}
