package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.*;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;

import java.io.File;

public interface WpRestClient {

    /**
     * Creates a new category.
     *
     * <p>Creates a category in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the category creation request; must not be {@code null}
     *
     * @return the created {@link WpCategory}
     */
    WpCategory createCategory(@NonNull WpCategoryCreateUpdateRequest creationRequest);

    // TODO: javadoc
    WpMedia createMedia(@NonNull File file, @NonNull String fileName, @NonNull String mimeType);

    /**
     * Creates a new post.
     *
     * <p>Creates a post in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the post creation request; must not be {@code null}
     *
     * @return the created {@link WpPost}
     */
    WpPost createPost(@NonNull WpPostCreateUpdateRequest creationRequest);

    /**
     * Creates a new tag.
     *
     * <p>Creates a tag in WordPress using the provided request payload.</p>
     *
     * @param creationRequest
     *         the tag creation request; must not be {@code null}
     *
     * @return the created {@link WpTag}
     */
    WpTag createTag(@NonNull WpTagCreateUpdateRequest creationRequest);

    /**
     * Deletes a category by its unique identifier.
     *
     * @param id
     *         the ID of the category to delete; must not be {@code null}
     *
     * @return the category deletion response
     */
    WpCategoryDeletionResponse deleteCategory(@NonNull Long id);

    /**
     * Permanently deletes a post by its unique identifier.
     *
     * @param id
     *         the ID of the post to delete; must not be {@code null}
     *
     * @return the post deletion response
     */
    WpPostDeletionResponse deletePost(@NonNull Long id);

    /**
     * Deletes a tag by its unique identifier.
     *
     * @param id
     *         the ID of the tag to delete; must not be {@code null}
     *
     * @return the tag deletion response
     */
    WpTagDeletionResponse deleteTag(@NonNull Long id);

    /**
     * Retrieves a category by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the category to retrieve; must not be {@code null}
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpCategory}
     */
    WpCategory getCategory(@NonNull Long id, WpContext context);

    /**
     * Retrieves a post by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the post to retrieve; must not be {@code null}
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpPost}
     */
    WpPost getPost(@NonNull Long id, WpContext context);

    /**
     * Retrieves a post by its unique identifier using the given context and optional password.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * <p>If the post is password protected, the provided password is used to access protected content.
     * If the password is missing or incorrect, protected fields such as content or excerpt may be omitted or flagged as
     * protected in the response.</p>
     *
     * @param id
     *         the ID of the post to retrieve; must not be {@code null}
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     * @param password
     *         the password for protected content; may be {@code null} when not required
     *
     * @return the matching {@link WpPost}
     */
    WpPost getPost(@NonNull Long id, WpContext context, String password);

    /**
     * Retrieves a tag by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the tag to retrieve; must not be {@code null}
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpTag}
     */
    WpTag getTag(@NonNull Long id, WpContext context);

    /**
     * Retrieves a paginated list of categories using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpCategoryQuery}, for example by filtering on parent,
     * search term, include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param categoryQuery
     *         additional category query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpCategory} items
     */
    WpPagedResponse<WpCategory> listCategories(@NonNull WpPagingQuery pageQuery, WpCategoryQuery categoryQuery);

    /**
     * Retrieves a paginated list of posts using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpPostQuery}, for example by filtering on search criteria,
     * include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param postQuery
     *         additional post query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpPost} items
     */
    WpPagedResponse<WpPost> listPosts(@NonNull WpPagingQuery pageQuery, WpPostQuery postQuery);

    /**
     * Retrieves a paginated list of tags using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpTagQuery}, for example by filtering on search criteria,
     * include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param tagQuery
     *         additional tag query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpTag} items
     */
    WpPagedResponse<WpTag> listTags(@NonNull WpPagingQuery pageQuery, WpTagQuery tagQuery);

    /**
     * Moves a post to the trash by its unique identifier.
     *
     * @param id
     *         the ID of the post to trash; must not be {@code null}
     *
     * @return the trashed {@link WpPost}
     */
    WpPost trashPost(@NonNull Long id);

    /**
     * Updates an existing category.
     *
     * <p>Updates a category in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the category to update; must not be {@code null}
     * @param updateRequest
     *         the category update request; must not be {@code null}
     *
     * @return the updated {@link WpCategory}
     */
    WpCategory updateCategory(@NonNull Long id, @NonNull WpCategoryCreateUpdateRequest updateRequest);

    /**
     * Updates an existing post.
     *
     * <p>Updates a post in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the post to update; must not be {@code null}
     * @param updateRequest
     *         the post update request; must not be {@code null}
     *
     * @return the updated {@link WpPost}
     */
    WpPost updatePost(@NonNull Long id, @NonNull WpPostCreateUpdateRequest updateRequest);

    /**
     * Updates an existing tag.
     *
     * <p>Updates a tag in WordPress using the provided request payload.</p>
     *
     * @param id
     *         the ID of the tag to update; must not be {@code null}
     * @param updateRequest
     *         the tag update request; must not be {@code null}
     *
     * @return the updated {@link WpTag}
     */
    WpTag updateTag(@NonNull Long id, @NonNull WpTagCreateUpdateRequest updateRequest);
}
