package io.github.evisentin.wordpress.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.domain.api.PostTypeAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpPostType;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_POST_TYPES_MAP_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_POST_TYPE_TYPEREFERENCE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link PostTypeAPIs}.
 *
 * <p>Provides access to WordPress post type definitions through the {@code /wp/v2/types} endpoint.</p>
 */
public class PostTypeApiClientModule extends ApiClientModule implements PostTypeAPIs {

    public PostTypeApiClientModule(final String apiUrl,
                                   final OkHttpClient httpClient,
                                   final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpPostType get(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/types/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_POST_TYPE_TYPEREFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpPostType> list() {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/types", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_POST_TYPES_MAP_TYPEREFERENCE);
    }
}
