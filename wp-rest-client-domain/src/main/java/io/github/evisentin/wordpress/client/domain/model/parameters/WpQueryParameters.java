package io.github.evisentin.wordpress.client.domain.model.parameters;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WpQueryParameters {

    public static final String AFTER = "after";
    public static final String AUTHOR = "author";
    public static final String AUTHOR_EXCLUDE = "author_exclude";

    public static final String BEFORE = "before";

    public static final String CATEGORIES = "categories";
    public static final String CATEGORIES_EXCLUDE = "categories_exclude";
    public static final String CONTEXT = "context";

    public static final String EXCLUDE = "exclude";

    public static final String FORCE = "force";

    public static final String HIDE_EMPTY = "hide_empty";

    public static final String INCLUDE = "include";

    public static final String MEDIA_TYPE = "media_type";
    public static final String MIME_TYPE = "mime_type";

    public static final String MODIFIED_AFTER = "modified_after";
    public static final String MODIFIED_BEFORE = "modified_before";

    public static final String OFFSET = "offset";
    public static final String ORDER = "order";
    public static final String ORDER_BY = "order_by";

    public static final String PAGE = "page";
    public static final String PARENT = "parent";
    public static final String PARENT_EXCLUDE = "parent_exclude";

    public static final String PER_PAGE = "per_page";
    public static final String POST = "post";

    public static final String SEARCH = "search";
    public static final String SLUG = "slug";
    public static final String STATUS = "status";
    public static final String STICKY = "sticky";

    public static final String TAGS = "tags";
    public static final String TAGS_EXCLUDE = "tags_exclude";
}
