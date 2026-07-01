/**
 * OkHttp-based implementations of WordPress REST API modules.
 *
 * <p>This package contains concrete implementations of the domain API contracts defined in
 * {@code io.github.evisentin.wordpress.rest.client.domain.api}. Each module targets a specific WordPress REST
 * APIresource and translates domain-level requests into HTTP requests executed through OkHttp.</p>
 *
 * <p>The modules support resource creation, retrieval, update, deletion,trash operations, pagination, query parameter
 * mapping, JSON serialization and deserialization, and multipart mediauploads.</p>
 *
 * <p>Common request execution logic is provided by
 * {@link io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.ApiClientModule}.</p>
 */
package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;
