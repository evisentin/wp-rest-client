/**
 * Defines the operation contracts exposed by the WordPress REST client.
 *
 * <p>This package contains resource-specific interfaces that model the capabilities of the WordPress REST API. Each
 * interface groups operations related to a particular resource domain, such as posts, categories, tags, media items,
 * post types, or post statuses.</p>
 *
 * <p>These interfaces are intended to be composed by higher-level client APIs such as
 * {@link io.github.evisentin.wordpress.client.domain.api.WpRestClient}, allowing implementations to expose a unified
 * entry point while keeping resource-specific concerns separated.</p>
 *
 * <p>Typical operations include:</p>
 * <ul>
 *     <li>Creating resources</li>
 *     <li>Retrieving resources by identifier</li>
 *     <li>Listing resources with pagination and filtering</li>
 *     <li>Updating resources</li>
 *     <li>Deleting or trashing resources</li>
 *     <li>Retrieving WordPress metadata such as post types and statuses</li>
 * </ul>
 *
 * <p>Implementations are expected to communicate with the standard WordPress REST API endpoints under
 * {@code /wp-json/wp/v2} or compatible APIs.</p>
 */
package io.github.evisentin.wordpress.client.domain.api.operations;
