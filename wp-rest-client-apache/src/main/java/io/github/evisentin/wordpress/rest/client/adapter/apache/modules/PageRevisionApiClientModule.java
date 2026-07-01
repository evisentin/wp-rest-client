package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers.PageRevisionQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.PageRevisionAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPageRevision;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageRevisionQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_PAGE_REVISION_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_PAGE_REVISION_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;

public class PageRevisionApiClientModule extends ApiClientModule implements PageRevisionAPIs {

    public PageRevisionApiClientModule(final String apiUrl,
                                       final CloseableHttpClient httpClient,
                                       final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPageRevision get(final long pageId, final long revisionId) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${pageId}/revisions/${revisionId}",
                Map.of(API_URL, apiUrl,
                        "pageId", pageId,
                        "revisionId", revisionId));
        return performGetRequest(builder, WP_PAGE_REVISION_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPageRevision> list(final long pageId,
                                                final @NonNull WpPaginationQuery paginationQuery,
                                                final WpPageRevisionQuery query) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${pageId}/revisions",
                Map.of(API_URL, apiUrl,
                        "pageId", pageId));

        builder.addParameter(PAGE, Integer.toString(paginationQuery.pageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));

        PageRevisionQueryParamMapper.map(builder, query);

        return performPagingRequest(builder, paginationQuery, WP_PAGE_REVISION_LIST_TYPEREFERENCE);
    }
}
