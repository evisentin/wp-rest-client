package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.domain.WpRestClient;

public interface WpJwtAuthRestClientFactory {
    WpRestClient create(String baseUrl, String jwtTokenEndPoint, String username, String password);
}
