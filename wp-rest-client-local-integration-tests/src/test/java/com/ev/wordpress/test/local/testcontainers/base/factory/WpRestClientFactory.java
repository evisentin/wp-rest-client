package com.ev.wordpress.test.local.testcontainers.base.factory;

import com.ev.wordpress.client.domain.api.WpRestClient;

public interface WpRestClientFactory {
    WpRestClient create(String baseUrl, String username, String password);
}
