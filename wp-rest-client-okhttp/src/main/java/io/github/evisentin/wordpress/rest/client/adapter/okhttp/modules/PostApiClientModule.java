package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers.PostQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.PostAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPostDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_POST_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_POST_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_POST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * OkHttp implementation of {@link PostAPIs}.
 *
 * <p>Provides access to WordPress post resources through the {@code /wp/v2/posts} endpoint, including
 * password-protected retrieval and trash operations.</p>
 */
public class PostApiClientModule extends ApiClientModule implements PostAPIs {

    public PostApiClientModule(final String apiUrl,
                               final OkHttpClient httpClient,
                               final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPost create(final @NonNull WpPostCreateUpdateRequest creationRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostDeletionResponse delete(long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    public WpPost get(final long id, final WpContext context) {
        return get(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpPost get(final long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> list(final @NonNull WpPagingQuery pageQuery, final WpPostQuery postQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost trash(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}",
                Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost update(final long id,
                         final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPEREFERENCE);
    }
}
