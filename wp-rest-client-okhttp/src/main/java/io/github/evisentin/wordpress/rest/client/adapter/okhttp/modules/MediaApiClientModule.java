package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers.MediaQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.MediaAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpMedia;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpMediaQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpMediaUpdateRequest;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpMediaDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.*;

import java.io.File;
import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_MEDIA_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_MEDIA_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static okhttp3.MultipartBody.FORM;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link MediaAPIs}.
 *
 * <p>Provides access to WordPress media resources through the {@code /wp/v2/media} endpoint, including multipart file
 * uploads.</p>
 */
public class MediaApiClientModule extends ApiClientModule implements MediaAPIs {

    public MediaApiClientModule(final String apiUrl,
                                final OkHttpClient httpClient,
                                final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpMedia create(final @NonNull File file,
                          final @NonNull String fileName,
                          final @NonNull String mimeType) {

        if (isBlank(fileName)) throw new IllegalArgumentException("fileName cannot be blank");
        if (isBlank(mimeType)) throw new IllegalArgumentException("mimeType cannot be blank");

        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mimeType));

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));

        return performPostWithMultiPartBody(builder, requestBody, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMediaDeletionResponse delete(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_MEDIA_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia get(final long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_MEDIA_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpMedia> list(final @NonNull WpPaginationQuery paginationQuery,
                                         final WpMediaQuery mediaQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(paginationQuery.pageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));

        MediaQueryParamMapper.map(builder, mediaQuery);

        return performPagingRequest(builder, paginationQuery, WP_MEDIA_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpMedia update(final long id,
                          final @NonNull WpMediaUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/media/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_MEDIA_TYPEREFERENCE);
    }
}
