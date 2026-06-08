package io.github.evisentin.wordpress.client.domain.api;

import io.github.evisentin.wordpress.client.domain.api.operations.*;

/**
 * Main entry point for interacting with the WordPress REST API.
 *
 * <p>This interface aggregates operations for managing WordPress resources such as posts, categories, tags, and media
 * items.</p>
 *
 * <p>The client provides a strongly typed abstraction over the WordPress REST API, exposing high-level operations for
 * creating, retrieving, updating, listing, trashing, and deleting resources.</p>
 *
 * <p>Supported resource domains include:</p>
 *
 * <ul>
 *     <li>{@link CategoryOperations} for category management</li>
 *     <li>{@link CommentOperations} for category management</li>
 *     <li>{@link MediaOperations} for media upload and management</li>
 *     <li>{@link PostOperations} for post management</li>
 *     <li>{@link PostTypesOperations} for post types management</li>
 *     <li>{@link TagOperations} for tag management</li>
 * </ul>
 *
 * <p>Implementations are expected to communicate with the standard WordPress REST API endpoints under
 * {@code /wp-json/wp/v2} or compatible APIs.</p>
 */
public interface WpRestClient extends CategoryOperations, CommentOperations, MediaOperations, PostOperations, PostTypesOperations, StatusOperations, TagOperations {
}
