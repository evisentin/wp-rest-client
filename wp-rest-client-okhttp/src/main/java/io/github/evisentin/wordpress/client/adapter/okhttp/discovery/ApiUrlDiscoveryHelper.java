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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiUrlDiscoveryHelper {

    private static final Pattern LINK_PATTERN = Pattern.compile("<([^>]+)>\\s*;\\s*rel=\"([^\"]+)\"");

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
