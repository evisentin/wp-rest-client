package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers.PageQueryParamMapper;
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
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_PAGE_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_PAGE_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_PAGE_TYPEREFERENCE;
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
                               final CloseableHttpClient httpClient,
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
    public WpPage create(final @NonNull WpPageCreateUpdateRequest creationRequest, final Map<String, String> extraQueryParams) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages", Map.of(API_URL, apiUrl));

        emptyIfNull(extraQueryParams)
                .forEach(builder::addParameter);

        return performPostWithBody(builder, creationRequest, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPageDeletionResponse delete(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.TRUE.toString());

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
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());
        if (isNotBlank(password))
            builder.addParameter("password", password);

        return performGetRequest(builder, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpPage> list(final @NonNull WpPaginationQuery paginationQuery,
                                        final WpPageQuery pageQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(paginationQuery.pageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(paginationQuery.pageSize()));

        PageQueryParamMapper.map(builder, pageQuery);

        return performPagingRequest(builder, paginationQuery, WP_PAGE_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPage trash(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addParameter(FORCE, Boolean.FALSE.toString());

        return performDeleteRequest(builder, WP_PAGE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPage update(final long id, final @NonNull WpPageCreateUpdateRequest updateRequest) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/pages/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_PAGE_TYPEREFERENCE);
    }
}
