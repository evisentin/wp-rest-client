package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpTag;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;

/**
 * Defines operations for managing WordPress tags through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving, updating,
 * listing, and deleting {@link WpTag} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as
 * pagination, filtering, sorting, and retrieval contexts.</p>
 *
 * <p>Implementations are expected to communicate with the
 * {@code /wp-json/wp/v2/tags} endpoint or compatible APIs.</p>
 */
public interface TagOperations {
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
     * Deletes a tag by its unique identifier.
     *
     * @param id
     *         the ID of the tag to delete; must not be {@code null}
     *
     * @return the tag deletion response
     */
    WpTagDeletionResponse deleteTag(@NonNull Long id);

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
