package io.github.evisentin.wordpress.client.adapter.okhttp.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.domain.api.PostStatusAPIs;
import io.github.evisentin.wordpress.client.domain.model.WpStatus;
import lombok.NonNull;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.Map;

import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_STATUSES_MAP_TYPEREFERENCE;
import static io.github.evisentin.wordpress.client.adapter.okhttp.modules.TypeReferences.WP_STATUS_TYPE_REFERENCE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp implementation of {@link PostStatusAPIs}.
 *
 * <p>Provides access to WordPress post status definitions through the {@code /wp/v2/statuses} endpoint.</p>
 */
public class PostStatusApiClientModule extends ApiClientModule implements PostStatusAPIs {

    public PostStatusApiClientModule(final String apiUrl,
                                     final OkHttpClient httpClient,
                                     final ObjectMapper mapper) {
        super(apiUrl, httpClient, mapper);
    }

    @Override
    @SneakyThrows
    public WpStatus get(final @NonNull String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("name cannot be blank");

        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/statuses/${name}", Map.of(API_URL, apiUrl, "name", name));

        return performGetRequest(builder, WP_STATUS_TYPE_REFERENCE);
    }

    @Override
    @SneakyThrows
    public Map<String, WpStatus> list() {
        final HttpUrl.Builder builder = urlBuilder("${apiUrl}/wp/v2/statuses", Map.of(API_URL, apiUrl));

        return performGetRequest(builder, WP_STATUSES_MAP_TYPEREFERENCE);
    }
}
