package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers.PostRevisionQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.PostRevisionAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPostRevision;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPostRevisionQuery;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_POST_REVISION_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_POST_REVISTION_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;

public class PostRevisionApiClientModule extends ApiClientModule implements PostRevisionAPIs {

    public PostRevisionApiClientModule(final String apiUrl,
                                       final CloseableHttpClient httpClient,
                                       final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPostRevision get(final long postId, final long revisionId) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${postId}/revisions/${revisionId}",
                Map.of(API_URL, apiUrl,
                        "postId", postId,
                        "revisionId", revisionId));
        return performGetRequest(builder, WP_POST_REVISION_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPostRevision> list(final long postId,
                                                final @NonNull WpPagingQuery pageQuery,
                                                final WpPostRevisionQuery postQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/posts/${postId}/revisions",
                Map.of(API_URL, apiUrl,
                        "postId", postId));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        PostRevisionQueryParamMapper.map(builder, postQuery);

        return performPagingRequest(builder, pageQuery, WP_POST_REVISTION_LIST_TYPEREFERENCE);
    }
}
