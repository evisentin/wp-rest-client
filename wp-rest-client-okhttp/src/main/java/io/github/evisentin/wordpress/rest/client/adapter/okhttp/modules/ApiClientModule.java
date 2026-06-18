package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPagingQuery;
import lombok.RequiredArgsConstructor;
import lombok.val;
import okhttp3.*;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.HttpHeaders.ACCEPT;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.HttpHeaders.X_WP_TOTAL;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.HttpHeaders.X_WP_TOTAL_PAGES;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.http.MimeTypes.APPLICATION_JSON;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Base class for OkHttp-backed WordPress API modules.
 *
 * <p>Provides shared functionality for building endpoint URLs, executing HTTP requests, serializing request bodies,
 * deserializing responses, handling paginated responses, and submitting multipart requests.</p>
 *
 * <p>Concrete modules extend this class to implement resource-specific WordPress REST API operations.</p>
 */
@RequiredArgsConstructor
abstract class ApiClientModule {

    protected static final String API_URL = "apiUrl";
    protected final String apiUrl;
    protected final OkHttpClient httpClient;
    protected final ObjectMapper mapper;

    protected <T> T performDeleteRequest(final HttpUrl.Builder urlBuilder,
                                         final TypeReference<T> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .delete()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final ResponseBody body = response.body();

            return mapper.readValue(body.string(), responseType);
        }
    }

    protected <T> T performGetRequest(final HttpUrl.Builder urlBuilder,
                                      final TypeReference<T> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final ResponseBody body = response.body();

            return mapper.readValue(body.string(), responseType);
        }
    }

    protected <T> WpPagedResponse<T> performPagingRequest(final HttpUrl.Builder urlBuilder,
                                                          final WpPagingQuery pageQuery,
                                                          final TypeReference<List<T>> responseType) throws IOException {
        final Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final int totalItems = Integer.parseInt(requireNonNull(response.header(X_WP_TOTAL, "0")));
            final int totalPages = Integer.parseInt(requireNonNull(response.header(X_WP_TOTAL_PAGES, "0")));
            final String json = response.body().string();

            final List<T> items = mapper.readValue(json, responseType);

            return new WpPagedResponse<>(
                    items,
                    pageQuery.getPageSize(),
                    totalItems,
                    totalPages,
                    pageQuery.getPageNumber()
            );
        }
    }

    protected <T> T performPostWithBody(final HttpUrl.Builder urlBuilder,
                                        final Object requestBody,
                                        final TypeReference<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .post(RequestBody.create(toJson(requestBody), MEDIA_TYPE_APPLICATION_JSON))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            val json = response.body().string();
            return mapper.readValue(json, responseType);
        }
    }

    protected <T> T performPostWithMultiPartBody(final HttpUrl.Builder urlBuilder,
                                                 final MultipartBody requestBody,
                                                 final TypeReference<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header(ACCEPT, APPLICATION_JSON)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            val json = response.body().string();
            return mapper.readValue(json, responseType);
        }
    }

    protected String toJson(final Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    protected HttpUrl.Builder urlBuilder(final String path, final Map<String, Object> pathParams) {
        final String substituted = new StringSubstitutor(emptyIfNull(pathParams)).replace(path);
        return Objects.requireNonNull(HttpUrl.parse(substituted)).newBuilder();
    }

    private static Map<String, Object> emptyIfNull(final Map<String, Object> map) {
        return ofNullable(map).orElseGet(Collections::emptyMap);
    }
}
