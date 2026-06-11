package io.github.evisentin.wordpress.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.CommentQueryParamMapper;
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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_COMMENT_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_COMMENT_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_COMMENT_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * OkHttp implementation of {@link CommentAPIs}.
 *
 * <p>Provides access to WordPress comment resources through the {@code /wp/v2/comments} endpoint, including
 * password-protected retrieval and trash operations.</p>
 */
public class CommentApiClientModule extends ApiClientModule implements CommentAPIs {

    public CommentApiClientModule(final String apiUrl,
                                  final OkHttpClient httpClient,
                                  final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpComment create(final @NonNull WpCommentCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getContent())) throw new IllegalArgumentException("content cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCommentDeletionResponse delete(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

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
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpComment> list(final @NonNull WpPagingQuery pageQuery,
                                           final WpCommentQuery commentQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CommentQueryParamMapper.map(builder, commentQuery);

        return performPagingRequest(builder, pageQuery, WP_COMMENT_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment trash(long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_COMMENT_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpComment update(long id, final @NonNull WpCommentCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/comments/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_COMMENT_TYPEREFERENCE);
    }
}
