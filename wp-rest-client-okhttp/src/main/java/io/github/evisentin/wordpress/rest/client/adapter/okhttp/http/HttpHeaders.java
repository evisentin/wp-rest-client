package io.github.evisentin.wordpress.rest.client.adapter.okhttp.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class containing HTTP header name http.
 *
 * <p>
 * This class provides commonly used standard HTTP headers as well as WordPress-specific headers returned by the
 * WordPress REST API.
 * </p>
 *
 * <p>
 * The class cannot be instantiated.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpHeaders {

    /**
     * {@code Accept} header.
     */
    public static final String ACCEPT = "Accept";

    /**
     * {@code Authorization} header.
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * {@code Content-Type} header.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * {@code Content-Length} header.
     */
    public static final String CONTENT_LENGTH = "Content-Length";

    /**
     * {@code User-Agent} header.
     */
    public static final String USER_AGENT = "User-Agent";

    /**
     * {@code Host} header.
     */
    public static final String HOST = "Host";

    /**
     * {@code Connection} header.
     */
    public static final String CONNECTION = "Connection";

    /**
     * {@code Cache-Control} header.
     */
    public static final String CACHE_CONTROL = "Cache-Control";

    /**
     * {@code Accept-Encoding} header.
     */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    /**
     * {@code Accept-Language} header.
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * {@code Referer} header.
     */
    public static final String REFERER = "Referer";

    /**
     * {@code Cookie} header.
     */
    public static final String COOKIE = "Cookie";

    /**
     * {@code Set-Cookie} response header.
     */
    public static final String SET_COOKIE = "Set-Cookie";

    /**
     * {@code Location} response header.
     */
    public static final String LOCATION = "Location";

    // ------------------------------------------------------------------------
    // WordPress-specific headers
    // ------------------------------------------------------------------------

    /**
     * {@code X-WP-Total} response header.
     * <p>
     * Indicates the total number of items available across all pages.
     * </p>
     */
    public static final String X_WP_TOTAL = "X-WP-Total";

    /**
     * {@code X-WP-TotalPages} response header.
     * <p>
     * Indicates the total number of pages available.
     * </p>
     */
    public static final String X_WP_TOTAL_PAGES = "X-WP-TotalPages";
}
