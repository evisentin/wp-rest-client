package io.github.evisentin.wordpress.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.apache.query.mappers.CategoryQueryParamMapper;
import io.github.evisentin.wordpress.client.domain.api.CategoryAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpCategory;
import io.github.evisentin.wordpress.client.domain.model.WpPagedResponse;
import io.github.evisentin.wordpress.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.client.domain.model.query.WpCategoryQuery;
import io.github.evisentin.wordpress.client.domain.model.query.WpPagingQuery;
import io.github.evisentin.wordpress.client.domain.model.requests.WpCategoryCreateUpdateRequest;
import io.github.evisentin.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_CATEGORY_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.apache.modules.TypeReferences.WP_CATEGORY_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Apache HttpClient implementation of {@link CategoryAPIs}.
 *
 * <p>Provides access to WordPress category resources through the {@code /wp/v2/categories} endpoint.</p>
 */
public class CategoryApiClientModule extends ApiClientModule implements CategoryAPIs {

    public CategoryApiClientModule(final String apiUrl,
                                   final CloseableHttpClient httpClient,
                                   final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpCategory create(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse delete(final long id) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory get(final long id, final WpContext context) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));
        builder.addParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> list(final @NonNull WpPagingQuery pageQuery,
                                            final WpCategoryQuery categoryQuery) {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        builder.addParameter(PAGE, Integer.toString(pageQuery.getPageNumber()));
        builder.addParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory update(final long id,
                             final @NonNull WpCategoryCreateUpdateRequest updateRequest) {

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPEREFERENCE);
    }
}
