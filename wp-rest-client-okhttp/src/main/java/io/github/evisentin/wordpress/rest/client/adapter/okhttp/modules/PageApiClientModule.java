package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.okhttp.query.mappers.PageQueryParamMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.PageAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPage;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPageQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.query.WpPaginationQuery;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPageCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_PAGE_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_PAGE_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_PAGE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.rest.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Apache HttpClient implementation of {@link PageAPIs}.
 *
 * <p>Provides access to WordPress page resources through the {@code /wp/v2/pages} endpoint, including support for
 * password-protected content and trash operations.</p>
 */
public class PageApiClientModule extends ApiClientModule implements PageAPIs {

    public PageApiClientModule(final String apiUrl,
                               final OkHttpClient httpClient,
                               final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPage create(final @NonNull WpPageCreateUpdateRequest creationRequest) {
        return create(creationRequest, null);
    }

    @Override
    @SneakyThrows
    public WpPage create(final @NonNull WpPageCreateUpdateRequest creationRequest,
                         final Map<String, String> extraQueryParams) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages", Map.of(API_URL, apiUrl));

        emptyIfNull(extraQueryParams)
                .forEach(builder::addQueryParameter);

        return performPostWithBody(builder, creationRequest, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPageDeletionResponse delete(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_PAGE_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPage get(final long id, final WpContext context) {
        return get(id, context, null);
    }

    @Override
    @SneakyThrows
    public WpPage get(final long id, final WpContext context, final String password) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        if (isNotBlank(password))
            builder.addQueryParameter("password", password);

        return performGetRequest(builder, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPage> list(final @NonNull WpPaginationQuery paginationQuery,
                                        final WpPageQuery query) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(paginationQuery.pageNumber()));
        builder.addQueryParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));

        PageQueryParamMapper.map(builder, query);

        return performPagingRequest(builder, paginationQuery, WP_PAGE_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPage trash(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPage update(final long id,
                         final @NonNull WpPageCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_PAGE_TYPEREFERENCE);
    }
}
