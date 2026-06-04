package io.github.evisentin.wordpress.client.adapter.okhttp.discovery;

import io.github.evisentin.wordpress.client.domain.exception.ApiUrlNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal utility for discovering the WordPress REST API base URL.
 *
 * <p>This helper implements the WordPress REST API discovery mechanism by issuing an HTTP {@code HEAD} request against
 * a site URL and inspecting {@code Link} response headers for the standard WordPress API relation
 * {@code https://api.w.org/}.</p>
 *
 * <p>When a matching relation is found, the corresponding API endpoint URL is extracted and returned. Trailing slashes
 * are removed to ensure a consistent base URL format.</p>
 *
 * <p>This class is intended for internal use by the client implementation and is not part of the public API. Consumers
 * should rely on higher-level client abstractions rather than invoking this utility directly.</p>
 *
 * <p>The implementation follows the WordPress REST API discovery process described by WordPress and compatible API
 * providers.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiUrlDiscoveryHelper {

    private static final Pattern LINK_PATTERN = Pattern.compile("<([^>]+)>\\s*;\\s*rel=\"([^\"]+)\"");

    /**
     * Discovers the WordPress REST API base URL for a site.
     *
     * <p>An HTTP {@code HEAD} request is sent to the provided site URL and the returned {@code Link} headers are
     * inspected for a relation of {@code https://api.w.org/}. The associated URL is returned as the API endpoint.</p>
     *
     * @param httpClient
     *         the OkHttp client used to perform the discovery request; must not be {@code null}
     * @param baseUrl
     *         the WordPress site URL from which discovery should start; must not be {@code null}
     *
     * @return the discovered WordPress REST API base URL
     *
     * @throws ApiUrlNotFoundException
     *         if no WordPress REST API discovery link is present in the response
     */

    @SneakyThrows
    public static String resolveApiUrl(final @NonNull OkHttpClient httpClient,
                                       final @NonNull String baseUrl) {

        final Request request = new Request.Builder()
                .url(baseUrl)
                .head()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            final Headers headers = response.headers();

            for (String linkHeader : headers.values("Link")) {
                final Matcher matcher = LINK_PATTERN.matcher(linkHeader);

                while (matcher.find()) {
                    final String url = matcher.group(1);
                    final String rel = matcher.group(2);

                    if ("https://api.w.org/".equals(rel)) {
                        return Strings.CI.removeEnd(url, "/");
                    }
                }
            }
        }

        throw new ApiUrlNotFoundException(baseUrl);
    }
}
