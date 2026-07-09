package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers.SearchQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.SearchAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.WpSearchResult;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpSearchQuery;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_SEARCH_REULT_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;

/**
 * OkHttp implementation of {@link SearchAPIs}.
 *
 * <p>Provides access to WordPress search API through the {@code /wp/v2/sesarch} endpoint.</p>
 */
public class SearchApiClientModule extends ApiClientModule implements SearchAPIs {

    public SearchApiClientModule(final String apiUrl,
                                 final OkHttpClient httpClient,
                                 final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpSearchResult> search(final @NonNull WpPaginationQuery paginationQuery,
                                                  final @NonNull WpSearchQuery query) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/search", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(paginationQuery.pageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));
        SearchQueryParamMapper.map(builder, query);
        return performPagingRequest(builder, paginationQuery, WP_SEARCH_REULT_LIST_TYPEREFERENCE);
    }
}
