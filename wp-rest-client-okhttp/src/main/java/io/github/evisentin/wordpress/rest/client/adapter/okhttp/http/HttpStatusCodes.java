package io.github.evisentin.wordpress.rest.client.adapter.okhttp.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for HTTP response status codes.
 *
 * <p>
 * This class defines commonly used HTTP status codes as integer http, based on <a
 * href="https://www.rfc-editor.org/rfc/rfc9110">RFC 9110</a> and related specifications.
 * </p>
 *
 * <p>
 * Status codes are grouped by their standard categories:
 * </p>
 * <ul>
 *     <li><b>1xx</b> – Informational responses</li>
 *     <li><b>2xx</b> – Successful responses</li>
 *     <li><b>3xx</b> – Redirection messages</li>
 *     <li><b>4xx</b> – Client error responses</li>
 *     <li><b>5xx</b> – Server error responses</li>
 * </ul>
 *
 * <p>
 * This class is not intended to be instantiated.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpStatusCodes {

    // -------------------------------------------------------------------------
    // 1xx Informational
    // -------------------------------------------------------------------------

    /**
     * {@code 100 Continue}
     */
    public static final int CONTINUE = 100;

    /**
     * {@code 101 Switching Protocols}
     */
    public static final int SWITCHING_PROTOCOLS = 101;

    /**
     * {@code 102 Processing}
     */
    public static final int PROCESSING = 102;

    // -------------------------------------------------------------------------
    // 2xx Success
    // -------------------------------------------------------------------------

    /**
     * {@code 200 OK}
     */
    public static final int OK = 200;

    /**
     * {@code 201 Created}
     */
    public static final int CREATED = 201;

    /**
     * {@code 202 Accepted}
     */
    public static final int ACCEPTED = 202;

    /**
     * {@code 204 No Content}
     */
    public static final int NO_CONTENT = 204;

    // -------------------------------------------------------------------------
    // 3xx Redirection
    // -------------------------------------------------------------------------

    /**
     * {@code 300 Multiple Choices}
     */
    public static final int MULTIPLE_CHOICES = 300;

    /**
     * {@code 301 Moved Permanently}
     */
    public static final int MOVED_PERMANENTLY = 301;

    /**
     * {@code 302 Found}
     */
    public static final int FOUND = 302;

    /**
     * {@code 304 Not Modified}
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * {@code 307 Temporary Redirect}
     */
    public static final int TEMPORARY_REDIRECT = 307;

    /**
     * {@code 308 Permanent Redirect}
     */
    public static final int PERMANENT_REDIRECT = 308;

    // -------------------------------------------------------------------------
    // 4xx Client Error
    // -------------------------------------------------------------------------

    /**
     * {@code 400 Bad Request}
     */
    public static final int BAD_REQUEST = 400;

    /**
     * {@code 401 Unauthorized}
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * {@code 403 Forbidden}
     */
    public static final int FORBIDDEN = 403;

    /**
     * {@code 404 Not Found}
     */
    public static final int NOT_FOUND = 404;

    /**
     * {@code 405 Method Not Allowed}
     */
    public static final int METHOD_NOT_ALLOWED = 405;

    /**
     * {@code 409 Conflict}
     */
    public static final int CONFLICT = 409;

    /**
     * {@code 415 Unsupported Media Type}
     */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * {@code 429 Too Many Requests}
     */
    public static final int TOO_MANY_REQUESTS = 429;

    // -------------------------------------------------------------------------
    // 5xx Server Error
    // -------------------------------------------------------------------------

    /**
     * {@code 500 Internal Server Error}
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * {@code 501 Not Implemented}
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * {@code 502 Bad Gateway}
     */
    public static final int BAD_GATEWAY = 502;

    /**
     * {@code 503 Service Unavailable}
     */
    public static final int SERVICE_UNAVAILABLE = 503;

    /**
     * {@code 504 Gateway Timeout}
     */
    public static final int GATEWAY_TIMEOUT = 504;
}
