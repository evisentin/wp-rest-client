package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.domain.api.WpRestClient;

public interface WpJwtAuthRestClientFactory {
    WpRestClient create(String baseUrl, String jwtTokenUrl, String username, String password);
}
