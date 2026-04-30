package com.ev.wordpress.client.adapter.okhttp.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;

import static com.ev.wordpress.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON;
import static com.ev.wordpress.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON_CHARSET_UTF_8;
import static java.util.Objects.requireNonNull;
import static okhttp3.MediaType.parse;

/**
 * A utility class providing predefined {@link MediaType} instances for common MIME types.
 * <p>
 * These http are useful when building HTTP requests with specific content types using OkHttp.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MediaTypes {

    /**
     * {@code application/json} media type.
     */
    public static final MediaType MEDIA_TYPE_APPLICATION_JSON = requireNonNull(parse(APPLICATION_JSON));

    /**
     * {@code application/json; charset=utf-8} media type.
     */
    public static final MediaType MEDIA_TYPE_APPLICATION_JSON_CHARSET_UTF_8 = requireNonNull(parse(APPLICATION_JSON_CHARSET_UTF_8));
}
