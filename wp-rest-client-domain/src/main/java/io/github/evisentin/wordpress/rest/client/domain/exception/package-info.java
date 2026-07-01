/**
 * Contains exception types used by the WordPress REST client.
 *
 * <p>
 * These exceptions represent error responses returned by the WordPress REST API. Each concrete exception typically
 * corresponds to a specific HTTP status code (e.g., 400, 401, 403, 404) and wraps the WpError payload returned by the
 * API.
 * </p>
 *
 * <p>
 * All exceptions in this package extend {@code WpException}, which is an unchecked exception
 * ({@code RuntimeException}). This allows client code to handle API errors explicitly where needed, without requiring
 * mandatory exception declarations.
 * </p>
 *
 * <p>Typical usage:</p>
 * <ul>
 *   <li>{@code WpBadRequestException} - invalid request (HTTP 400)</li>
 *   <li>{@code WpUnauthorizedException} - authentication required or failed (HTTP 401)</li>
 *   <li>{@code WpForbiddenException} - insufficient permissions (HTTP 403)</li>
 *   <li>{@code WpNotFoundException} - resource not found (HTTP 404)</li>
 * </ul>
 */
package io.github.evisentin.wordpress.rest.client.domain.exception;
