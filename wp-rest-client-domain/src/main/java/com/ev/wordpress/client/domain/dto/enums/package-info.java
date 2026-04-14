/**
 * Enumeration types representing fixed value sets used by the WordPress REST API.
 *
 * <p>
 * This package contains enums that map Java constants to their corresponding string representations expected by the
 * WordPress API.
 * </p>
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
package com.ev.wordpress.client.domain.dto.enums;
