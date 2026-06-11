package io.github.evisentin.wordpress.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.TagQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.TagAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpTag;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpTagQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpTagCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_TAG_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_TAG_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_TAG_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link TagAPIs}.
 *
 * <p>Provides access to WordPress tag resources through the{@code /wp/v2/tags} endpoint.</p>
 */
public class TagApiClientModule extends ApiClientModule implements TagAPIs {

    public TagApiClientModule(final String apiUrl,
                              final OkHttpClient httpClient,
                              final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpTag create(final @NonNull WpTagCreateUpdateRequest creationRequest) {

        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTagDeletionResponse delete(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}",
                Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_TAG_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag get(final long id, final WpContext context) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_TAG_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpTag> list(final @NonNull WpPagingQuery pageQuery, final WpTagQuery tagQuery) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        TagQueryParamMapper.map(builder, tagQuery);

        return performPagingRequest(builder, pageQuery, WP_TAG_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpTag update(final long id,
                        final @NonNull WpTagCreateUpdateRequest updateRequest) {

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/tags/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_TAG_TYPEREFERENCE);
    }
}
