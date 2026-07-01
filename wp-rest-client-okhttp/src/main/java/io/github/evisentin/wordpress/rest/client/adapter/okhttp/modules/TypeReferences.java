package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.evisentin.wordpress.rest.client.domain.model.*;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.*;

import java.util.List;
import java.util.Map;

interface TypeReferences {

    // Categories
    TypeReference<WpCategory> WP_CATEGORY_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpCategory>> WP_CATEGORY_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpCategoryDeletionResponse> WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Comments
    TypeReference<WpComment> WP_COMMENT_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpComment>> WP_COMMENT_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpCommentDeletionResponse> WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Media
    TypeReference<WpMedia> WP_MEDIA_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpMedia>> WP_MEDIA_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpMediaDeletionResponse> WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Pages
    TypeReference<WpPage> WP_PAGE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpPage>> WP_PAGE_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpPageDeletionResponse> WP_PAGE_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Page revisions
    TypeReference<WpPageRevision> WP_PAGE_REVISION_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpPageRevision>> WP_PAGE_REVISION_LIST_TYPEREFERENCE = new TypeReference<>() {};

    // Posts
    TypeReference<WpPost> WP_POST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpPost>> WP_POST_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpPostDeletionResponse> WP_POST_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Post revisions
    TypeReference<WpPostRevision> WP_POST_REVISION_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpPostRevision>> WP_POST_REVISION_LIST_TYPEREFERENCE = new TypeReference<>() {};

    // Tags
    TypeReference<WpTag> WP_TAG_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpTag>> WP_TAG_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpTagDeletionResponse> WP_TAG_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    // Metadata
    TypeReference<WpPostType> WP_POST_TYPE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<Map<String, WpPostType>> WP_POST_TYPES_MAP_TYPEREFERENCE = new TypeReference<>() {};

    // Post Statuses
    TypeReference<WpStatus> WP_STATUS_TYPE_REFERENCE = new TypeReference<>() {};
    TypeReference<Map<String, WpStatus>> WP_STATUSES_MAP_TYPEREFERENCE = new TypeReference<>() {};

    // Taxonomies
    TypeReference<WpTaxonomyInfo> WP_TAXONOMY_TYPE_REFERENCE = new TypeReference<>() {};
    TypeReference<Map<String, WpTaxonomyInfo>> WP_TAXONOMIES_MAP_TYPEREFERENCE = new TypeReference<>() {};
}
