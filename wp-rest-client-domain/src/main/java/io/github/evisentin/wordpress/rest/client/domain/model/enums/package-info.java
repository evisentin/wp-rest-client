/**
 * Enumerations used by WordPress DTOs and query objects.
 *
 * <p>This package defines enum types representing constrained values used
 * in the WordPress REST API, such as post status, sorting direction, taxonomy types, and context values.
 *
 * <h2>Design</h2>
 * <ul>
 *     <li>All enums implement a common {@code WpHasValueEnum} interface,
 *     exposing the API value via {@code getValue()}.</li>
 *     <li>Each enum constant is associated with a string value used for
 *     JSON serialization.</li>
 *     <li>The {@code @JsonValue} annotation ensures seamless conversion
 *     when interacting with the API.</li>
 * </ul>
 */
package io.github.evisentin.wordpress.rest.client.domain.model.enums;
