package com.ev.wordpress.client.adapter.okhttp.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A utility class containing common MIME type strings used in HTTP communication.
 * <p>
 * Useful for setting or comparing content types in HTTP headers.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MimeTypes {

    /**
     * MIME type for JSON with UTF-8 encoding: {@code application/json; charset=utf-8}.
     */
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

    /**
     * MIME type for JSON without charset: {@code application/json}.
     */
    public static final String APPLICATION_JSON = "application/json";

    /**
     * MIME type for binary data: {@code application/octet-stream}.
     */
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    /**
     * MIME type for PDF documents: {@code application/pdf}.
     */
    public static final String APPLICATION_PDF = "application/pdf";
}
