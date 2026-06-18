package io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.TaxonomyAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTaxonomyInfo;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_TAXONOMIES_MAP_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.okhttp.modules.TypeReferences.WP_TAXONOMY_TYPE_REFERENCE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link TaxonomyAPIs}.
 *
 * <p>Provides access to WordPress tag resources through the{@code /wp/v2/taxonomies} endpoint.</p>
 */
public class TaxonomyApiClientModule extends ApiClientModule implements TaxonomyAPIs {

    public TaxonomyApiClientModule(final String apiUrl,
                                   final OkHttpClient httpClient,
                                   final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpTaxonomyInfo get(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/taxonomies/${name}",
                Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_TAXONOMY_TYPE_REFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpTaxonomyInfo> list() {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/taxonomies", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_TAXONOMIES_MAP_TYPEREFERENCE);
    }
}
