package io.github.evisentin.wordpress.rest.client.adapter.apache.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.api.TaxonomyAPIs;
import io.github.evisentin.wordpress.rest.client.domain.model.WpTaxonomyInfo;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.net.URIBuilder;

import java.util.Map;

import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_TAXONOMIES_MAP_TYPEREFERENCE;
import static io.github.evisentin.wordpress.rest.client.adapter.apache.modules.TypeReferences.WP_TAXONOMY_TYPE_REFERENCE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Apache HttpClient implementation of {@link TaxonomyAPIs}.
 *
 * <p>Provides access to WordPress tag resources through the {@code /wp/v2/taxonomies} endpoint.</p>
 */
public class TaxonomyApiClientModule extends ApiClientModule implements TaxonomyAPIs {

    public TaxonomyApiClientModule(final String apiUrl,
                                   final CloseableHttpClient httpClient,
                                   final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpTaxonomyInfo get(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/taxonomies/${name}",
                Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_TAXONOMY_TYPE_REFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpTaxonomyInfo> list() {
        final URIBuilder builder = urlBuilder("${apiUrl}/wp/v2/taxonomies", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_TAXONOMIES_MAP_TYPEREFERENCE);
    }
}
