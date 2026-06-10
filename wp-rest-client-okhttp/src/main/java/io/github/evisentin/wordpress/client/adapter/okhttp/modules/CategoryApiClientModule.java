package io.github.evisentin.wordpress.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.query.mappers.CategoryQueryParamMapper;
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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.okhttp.TypeReferences.WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.TypeReferences.WP_CATEGORY_LIST_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.TypeReferences.WP_CATEGORY_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.CONTEXT;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.FORCE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PAGE;
import static io.github.evisentin.wordpress.client.domain.model.parameters.WpQueryParameters.PER_PAGE;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link CategoryAPIs}.
 *
 * <p>Provides access to WordPress category resources through the {@code /wp/v2/categories} endpoint.</p>
 */
public class CategoryApiClientModule extends ApiClientModule implements CategoryAPIs {

    public CategoryApiClientModule(final @NonNull String apiUrl,
                                   final @NonNull OkHttpClient httpClient,
                                   final @NonNull ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpCategory create(final @NonNull WpCategoryCreateUpdateRequest creationRequest) {
        if (isBlank(creationRequest.getName()))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        return performPostWithBody(builder, creationRequest, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategoryDeletionResponse delete(final long id) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        // For tags/terms, WordPress does not support trashing, and the REST API explicitly
        // requires force to be true for delete.
        builder.addQueryParameter(FORCE, Boolean.TRUE.toString());

        return performDeleteRequest(builder, WP_CATEGORY_DELETION_RESPONSE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory get(final long id, final WpContext context) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        builder.addQueryParameter(CONTEXT, ofNullable(context).orElse(WpContext.VIEW).getValue());

        return performGetRequest(builder, WP_CATEGORY_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpPagedResponse<WpCategory> list(final @NonNull WpPagingQuery pageQuery,
                                            final WpCategoryQuery categoryQuery) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories", Map.of(API_URL, apiUrl));

        builder.addQueryParameter(PAGE, Integer.toString(pageQuery.getPageNumber()))
               .addQueryParameter(PER_PAGE, Integer.toString(pageQuery.getPageSize()));

        CategoryQueryParamMapper.map(builder, categoryQuery);

        return performPagingRequest(builder, pageQuery, WP_CATEGORY_LIST_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public WpCategory update(final long id,
                             final @NonNull WpCategoryCreateUpdateRequest updateRequest) {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/categories/${id}", Map.of(API_URL, apiUrl, "id", id));

        return performPostWithBody(builder, updateRequest, WP_CATEGORY_TYPEREFERENCE);
    }
}
