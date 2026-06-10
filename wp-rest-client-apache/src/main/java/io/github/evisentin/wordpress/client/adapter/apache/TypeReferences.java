package io.github.evisentin.wordpress.client.adapter.apache;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.evisentin.wordpress.client.domain.model.*;
import io.github.evisentin.wordpress.client.domain.model.responses.*;

import java.util.List;
import java.util.Map;

public interface TypeReferences {
    TypeReference<WpCategory> WP_CATEGORY_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpComment> WP_COMMENT_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpPost> WP_POST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpPostType> WP_POST_TYPE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpStatus> WP_STATUS_TYPE_REFERENCE = new TypeReference<>() {};
    TypeReference<WpMedia> WP_MEDIA_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpTag> WP_TAG_TYPEREFERENCE = new TypeReference<>() {};

    TypeReference<WpCategoryDeletionResponse> WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpCommentDeletionResponse> WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpMediaDeletionResponse> WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpPostDeletionResponse> WP_POST_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<WpTagDeletionResponse> WP_TAG_DELETION_RESPONSE_TYPEREFERENCE = new TypeReference<>() {};

    TypeReference<List<WpCategory>> WP_CATEGORY_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpComment>> WP_COMMENT_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpMedia>> WP_MEDIA_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpPost>> WP_POST_LIST_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<List<WpTag>> WP_TAG_LIST_TYPEREFERENCE = new TypeReference<>() {};

    TypeReference<Map<String, WpPostType>> WP_POST_TYPES_MAP_TYPEREFERENCE = new TypeReference<>() {};
    TypeReference<Map<String, WpStatus>> WP_STATUSES_MAP_TYPEREFERENCE = new TypeReference<>() {};
}
