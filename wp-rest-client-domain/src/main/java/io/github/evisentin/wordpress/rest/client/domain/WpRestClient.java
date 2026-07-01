package io.github.evisentin.wordpress.rest.client.domain;

import io.github.evisentin.wordpress.rest.client.domain.api.*;

/**
 * Main entry point for interacting with the WordPress REST API.
 *
 * <p>This interface aggregates operations for managing WordPress resources such as posts, categories, tags, and media
 * items.</p>
 *
 * <p>The client provides a strongly typed abstraction over the WordPress REST API, exposing high-level operations for
 * creating, retrieving, updating, listing, trashing, and deleting resources.</p>
 *
 * <p>Implementations are expected to communicate with the standard WordPress REST API endpoints under
 * {@code /wp-json/wp/v2} or compatible APIs.</p>
 */
public interface WpRestClient {

    /**
     * Returns the category API operations.
     *
     * @return category API operations
     */
    CategoryAPIs categories();

    /**
     * Returns the comment API operations.
     *
     * @return comment API operations
     */
    CommentAPIs comments();

    /**
     * Returns the media API operations.
     *
     * @return media API operations
     */
    MediaAPIs media();

    /**
     * Returns the page revisions API operations.
     *
     * @return page revision API operations
     */
    PageRevisionAPIs pageRevisions();

    /**
     * Returns the page API operations.
     *
     * @return page API operations
     */
    PageAPIs pages();

    /**
     * Returns the post revisions API operations.
     *
     * @return post revision API operations
     */
    PostRevisionAPIs postRevisions();

    /**
     * Returns the post status API operations.
     *
     * @return post status API operations
     */
    PostStatusAPIs postStatuses();

    /**
     * Returns the post type API operations.
     *
     * @return post type API operations
     */
    PostTypeAPIs postTypes();

    /**
     * Returns the post API operations.
     *
     * @return post API operations
     */
    PostAPIs posts();

    /**
     * Returns the tag API operations.
     *
     * @return tag API operations
     */
    TagAPIs tags();

    /**
     * Returns the taxonomy API operations.
     *
     * @return taxonomy API operations
     */
    TaxonomyAPIs taxonomies();
}
