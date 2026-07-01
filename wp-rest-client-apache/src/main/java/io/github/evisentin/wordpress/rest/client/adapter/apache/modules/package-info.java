/**
 * Apache HttpClient-based implementations of WordPress REST API modules.
 *
 * <p>This package contains concrete implementations of the domain API contracts defined in
 * {@code io.github.evisentin.wordpress.rest.client.domain.api}. Each module is responsible for interacting with a
 * specific WordPress REST API endpoint and translating domain requests into HTTP requests executed through Apache
 * HttpClient.</p>
 *
 * <p>The modules provide support for:</p>
 * <ul>
 *     <li>CRUD operations for WordPress resources</li>
 *     <li>Pagination handling and response metadata extraction</li>
 *     <li>Query parameter mapping from domain query objects</li>
 *     <li>JSON serialization and deserialization using Jackson</li>
 *     <li>File uploads for media resources</li>
 * </ul>
 *
 * <p>All module implementations share common HTTP request execution logic provided by {@link io.github.evisentin.wordpress.rest.client.adapter.apache.modules.ApiClientModule}.</p>
 */
package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;
