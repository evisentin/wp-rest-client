package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.model.WpMedia;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.query.WpMediaQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import lombok.NonNull;

import java.io.File;

/**
 * Defines operations for managing WordPress media items through the WordPress REST API.
 *
 * <p>This interface provides methods for uploading, retrieving, updating,
 * listing, and deleting {@link io.github.evisentin.wordpress.client.domain.model.WpMedia} resources.</p>
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
     * <p>Uploads a media file to WordPress using the provided file content and
     * metadata.</p>
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
     * Retrieves a paginated list of media items using optional filtering and sorting criteria.
     *
     * <p>The result set can be refined using {@link WpMediaQuery}, for example by filtering on parent,
     * search term, include or exclude lists, or sort order.</p>
     *
     * @param pageQuery
     *         the pagination settings, including page number and page size; must not be {@code null}
     * @param mediaQuery
     *         additional category query parameters; may be {@code null} when no extra filtering is needed
     *
     * @return a paginated response containing {@link WpMedia} items
     */
    WpPagedResponse<WpMedia> listMedia(@NonNull WpPagingQuery pageQuery, WpMediaQuery mediaQuery);
}
