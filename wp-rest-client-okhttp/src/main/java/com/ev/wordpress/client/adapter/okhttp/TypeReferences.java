package com.ev.wordpress.client.adapter.okhttp;

import com.ev.wordpress.client.domain.model.WpCategory;
import com.ev.wordpress.client.domain.model.WpPost;
import com.ev.wordpress.client.domain.model.WpTag;
import com.ev.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import com.ev.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

interface TypeReferences {
    TypeReference<WpCategory> WP_CATEGORY_TYPE = new TypeReference<>() {};
    TypeReference<WpPost> WP_POST_TYPE = new TypeReference<>() {};
    TypeReference<WpTag> WP_TAG_TYPE = new TypeReference<>() {};

    TypeReference<WpCategoryDeletionResponse> WP_CATEGORY_DELETION_RESPONSE_TYPE = new TypeReference<>() {};
    TypeReference<WpPostDeletionResponse> WP_POST_DELETION_RESPONSE_TYPE = new TypeReference<>() {};
    TypeReference<WpTagDeletionResponse> WP_TAG_DELETION_RESPONSE_TYPE = new TypeReference<>() {};

    TypeReference<List<WpCategory>> WP_CATEGORY_LIST_TYPE = new TypeReference<>() {};
    TypeReference<List<WpPost>> WP_POST_LIST_TYPE = new TypeReference<>() {};
    TypeReference<List<WpTag>> WP_TAG_LIST_TYPE = new TypeReference<>() {};
}
