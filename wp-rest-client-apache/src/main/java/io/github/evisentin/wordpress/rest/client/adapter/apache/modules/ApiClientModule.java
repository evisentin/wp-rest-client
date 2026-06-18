package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPagingQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.text.StringSubstitutor;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.apache.hc.core5.http.HttpHeaders.ACCEPT;

/**
 * Base class for Apache HttpClient-backed WordPress API modules.
 *
 * <p>This class provides common functionality for executing HTTP requests, handling paginated responses, performing
 * JSON serialization and deserialization, uploading files, and building endpoint URIs.</p>
 *
 * <p>Concrete module implementations extend this class to interact with specific WordPress REST API resources.</p>
 */
@RequiredArgsConstructor
abstract class ApiClientModule {
    /**
     * Template variable used when expanding endpoint URLs.
     */
    protected static final String API_URL = "apiUrl";

    protected final String apiUrl;
    protected final CloseableHttpClient httpClient;
    protected final ObjectMapper mapper;

    @SneakyThrows
    protected <T> T performDeleteRequest(final URIBuilder uriBuilder,
                                         final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpDelete request = new HttpDelete(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    protected <T> T performGetRequest(final URIBuilder uriBuilder,
                                      final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            return mapper.readValue(body, responseType);
        });
    }

    @SneakyThrows
    protected <T> WpPagedResponse<T> performPagingRequest(final URIBuilder uriBuilder,
                                                          final WpPagingQuery pageQuery,
                                                          final TypeReference<List<T>> responseType) {
        URI uri = uriBuilder.build();

        HttpGet request = new HttpGet(uri);
        request.setHeader(ACCEPT, "application/json");

        return httpClient.execute(request, response -> {
            int totalItems = ofNullable(response.getHeader("X-WP-Total"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            int totalPages = ofNullable(response.getHeader("X-WP-TotalPages"))
                    .map(header -> Integer.parseInt(header.getValue()))
                    .orElse(0);

            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            List<T> items = mapper.readValue(json, responseType);

            return new WpPagedResponse<>(
                    items,
                    pageQuery.getPageSize(),
                    totalItems,
                    totalPages,
                    pageQuery.getPageNumber()
            );
        });
    }

    @SneakyThrows
    protected <T> T performPostWithBody(final URIBuilder uriBuilder,
                                        final Object requestBody,
                                        final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpPost request = new HttpPost(uri);
        request.setHeader(ACCEPT, "application/json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        String jsonBody = mapper.writeValueAsString(requestBody);
        request.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

        return httpClient.execute(request, response -> {
            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            return mapper.readValue(json, responseType);
        });
    }

    @SneakyThrows
    protected <T> T performPostWithFileUpload(final URIBuilder uriBuilder,
                                              final File file,
                                              final String fileName,
                                              final String mimeType,
                                              final TypeReference<T> responseType) {

        URI uri = uriBuilder.build();

        HttpPost request = new HttpPost(uri);
        request.setHeader(ACCEPT, "application/json");

        request.setEntity(
                MultipartEntityBuilder.create()
                                      .addBinaryBody(
                                              "file",
                                              file,
                                              ContentType.parse(mimeType),
                                              fileName
                                      )

                                      .build()
        );

        return httpClient.execute(request, response -> {
            failOnEmptyResponseBody(response);

            String json = EntityUtils.toString(response.getEntity());
            return mapper.readValue(json, responseType);
        });
    }

    protected URIBuilder urlBuilder(final String path, final Map<String, Object> pathParams) throws URISyntaxException {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return new URIBuilder(substituted);
    }

    protected static <K, V> Map<K, V> emptyIfNull(final Map<K, V> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }

    private static void failOnEmptyResponseBody(final ClassicHttpResponse response) throws IOException {
        if (response.getEntity() == null) {
            throw new IOException("Empty response body");
        }
    }
}
