package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import lombok.NonNull;

/**
 * Defines operations for managing WordPress categories through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving, updating,
 * listing, and deleting {@link WpCategory} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as
 * pagination, filtering, sorting, and retrieval contexts.</p>
 *
 * <p>Implementations are expected to communicate with the
 * {@code /wp-json/wp/v2/categories} endpoint or compatible APIs.</p>
 */
public interface CategoryOperations {
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
}
