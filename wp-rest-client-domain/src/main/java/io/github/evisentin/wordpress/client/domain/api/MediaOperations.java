package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpMedia;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpMediaQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.responses.WpMediaDeletionResponse;
import lombok.NonNull;

import java.io.File;

/**
 * Defines operations for managing WordPress media items through the WordPress REST API.
 *
 * <p>This interface provides methods for creating, retrieving,
 * and listing {@link WpMedia} resources.</p>
 *
 * <p>Operations support standard WordPress REST API features such as
 * pagination, filtering, sorting, and retrieval contexts.</p>
 *
 * <p>Media operations typically involve handling binary file uploads together
 * with metadata such as MIME type, filename, captions, descriptions, and attachment information.</p>
 *
 * <p>Implementations are expected to communicate with the
 * {@code /wp-json/wp/v2/media} endpoint or compatible APIs.</p>
 */
public interface MediaOperations {

    /**
     * Creates a new media item.
     *
     * <p>Uploads a media file to WordPress using the provided file content
     * and metadata.</p>
     *
     * <p>The uploaded file is sent to the WordPress media endpoint and results
     * in the creation of a new {@link WpMedia} attachment resource.</p>
     *
     * @param file
     *         the file to upload; must not be {@code null}
     * @param fileName
     *         the filename to associate with the uploaded media item; must not be {@code null}
     * @param mimeType
     *         the MIME type of the uploaded file, for example {@code image/png}; must not be {@code null}
     *
     * @return the created {@link WpMedia}
     */
    WpMedia createMedia(@NonNull File file, @NonNull String fileName, @NonNull String mimeType);

    /**
     * Deletes a media item by its unique identifier.
     *
     * @param id
     *         the ID of the media item to delete; must not be {@code null}
     *
     * @return the media item deletion response
     */
    WpMediaDeletionResponse deleteMedia(@NonNull Long id);

    /**
     * Retrieves a media item by its unique identifier using the given context.
     *
     * <p>The context determines which fields are included in the response, for example
     * {@link WpContext#VIEW}, {@link WpContext#EDIT}, or {@link WpContext#EMBED}.</p>
     *
     * @param id
     *         the ID of the media item to retrieve; must not be {@code null}
     * @param context
     *         the retrieval context; may be {@code null}, in which case {@link WpContext#VIEW} is used
     *
     * @return the matching {@link WpMedia}
     */
    WpMedia getMedia(@NonNull Long id, WpContext context);

    /**
     * Retrieves a paginated list of media items using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpMediaQuery}, for example by filtering on parent,
     * search term, include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param mediaQuery
     *         additional media query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpMedia} items
     */
    WpPagedResponse<WpMedia> listMedia(@NonNull WpPagingQuery pageQuery, WpMediaQuery mediaQuery);
}
