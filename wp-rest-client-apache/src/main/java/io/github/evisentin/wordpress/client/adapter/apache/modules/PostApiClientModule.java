package io.github.evisentin.wordpress.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.PostQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.PostAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.WpPost;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPostQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_POST_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_POST_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_POST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Apache HttpClient implementation of {@link PostAPIs}.
 *
 * <p>Provides access to WordPress post resources through the {@code /wp/v2/posts} endpoint, including support for
 * password-protected content and trash operations.</p>
 */

public class PostApiClientModule extends ApiClientModule implements PostAPIs {

    public PostApiClientModule(final String apiUrl,
                               final CloseableHttpClient httpClient,
                               final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPost create(final @NonNull WpPostCreateUpdateRequest creationRequest) {

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPostDeletionResponse delete(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_POST_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost get(final long id, final WpContext context) {
        return get(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpPost get(final long id, final WpContext context, final String password) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addParameter("password", password);

        return performGetRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPost> list(final @NonNull WpPagingQuery pageQuery,
                                        final WpPostQuery postQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost trash(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_POST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPost update(final long id,
                         final @NonNull WpPostCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_POST_TYPEREFERENCE);
    }
}
