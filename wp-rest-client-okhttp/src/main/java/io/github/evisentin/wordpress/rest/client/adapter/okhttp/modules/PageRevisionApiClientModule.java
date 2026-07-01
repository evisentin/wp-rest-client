package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers.PageRevisionQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.PageRevisionAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPageRevision;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageRevisionQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_PAGE_REVISION_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_PAGE_REVISION_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;

public class PageRevisionApiClientModule extends ApiClientModule implements PageRevisionAPIs {

    public PageRevisionApiClientModule(final String apiUrl,
                                       final OkHttpClient httpClient,
                                       final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPageRevision get(final long pageId, final long revisionId) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${pageId}/revisions/${revisionId}",
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
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${pageId}/revisions",
                Map.of(API_URL, apiUrl,
                        "pageId", pageId));

        builder.addQueryParameter(PAGE, Integer.toString(paginationQuery.pageNumber()));
        builder.addQueryParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));

        PageRevisionQueryParamMapper.map(builder, query);

        return performPagingRequest(builder, paginationQuery, WP_PAGE_REVISION_LIST_TYPEREFERENCE);
    }
}
