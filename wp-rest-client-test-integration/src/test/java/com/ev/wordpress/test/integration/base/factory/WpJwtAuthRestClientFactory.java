package com.ev.wordpress.test.integration.base.factory;

import com.ev.wordpress.client.domain.api.WpRestClient;

public interface WpJwtAuthRestClientFactory {
    WpRestClient create(String baseUrl, String jwtTokenUrl, String username, String password);
}
