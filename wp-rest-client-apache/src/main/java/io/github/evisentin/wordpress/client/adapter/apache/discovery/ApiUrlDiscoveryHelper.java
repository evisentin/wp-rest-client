package io.github.evisentin.wordpress.client.adapter.apache.discovery;

import io.github.evisentin.wordpress.client.domain.exception.ApiUrlNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.Strings;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.Header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiUrlDiscoveryHelper {

    private static final Pattern LINK_PATTERN = Pattern.compile("<([^>]+)>\\s*;\\s*rel=\"([^\"]+)\"");

    @SneakyThrows
    public static String resolveApiUrl(final @NonNull CloseableHttpClient httpClient,
                                       final @NonNull String baseUrl) {

        final HttpHead request = new HttpHead(baseUrl);

        return httpClient.execute(request, response -> {

            for (Header header : response.getHeaders("Link")) {

                final Matcher matcher = LINK_PATTERN.matcher(header.getValue());

                while (matcher.find()) {
                    final String url = matcher.group(1);
                    final String rel = matcher.group(2);

                    if ("https://api.w.org/".equals(rel)) {
                        return Strings.CI.removeEnd(url, "/");
                    }
                }
            }

            throw new ApiUrlNotFoundException(baseUrl);
        });
    }
}
