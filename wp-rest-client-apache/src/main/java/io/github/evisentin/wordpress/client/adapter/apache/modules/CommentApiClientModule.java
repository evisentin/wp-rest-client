package io.github.evisentin.wordpress.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.CommentQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.CommentAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpComment;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCommentQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCommentCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCommentDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.apache.TypeReferences.WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.TypeReferences.WP_COMMENT_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.TypeReferences.WP_COMMENT_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Apache HttpClient implementation of {@link CommentAPIs}.
 *
 * <p>Provides access to WordPress comment resources through the {@code /wp/v2/comments} endpoint, including support
 * for password-protected content and comment trashing.</p>
 */
public class CommentApiClientModule extends ApiClientModule implements CommentAPIs {

    public CommentApiClientModule(final @NonNull String apiUrl,
                                  final @NonNull CloseableHttpClient httpClient,
                                  final @NonNull ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpComment create(final @NonNull WpCommentCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getContent())) throw new IllegalArgumentException("content cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCommentDeletionResponse delete(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment get(final long id, final WpContext context) {
        return get(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpComment get(final long id, final WpContext context, final String password) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addParameter("password", password);

        return performGetRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpComment> list(final @NonNull WpPagingQuery pageQuery,
                                           final WpCommentQuery commentQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CommentQueryParamMapper.map(builder, commentQuery);

        return performPagingRequest(builder, pageQuery, WP_COMMENT_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment trash(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment update(long id, final @NonNull WpCommentCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_COMMENT_TYPEREFERENCE);
    }
}
