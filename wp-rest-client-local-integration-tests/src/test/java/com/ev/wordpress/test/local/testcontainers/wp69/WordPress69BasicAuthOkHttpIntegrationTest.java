package com.ev.wordpress.test.local.testcontainers.wp69;

import com.ev.wordpress.test.local.testcontainers.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.local.testcontainers.base.factory.OkHttpWpRestClientFactory;
import com.ev.wordpress.test.local.testcontainers.base.factory.WpRestClientFactory;

import static com.ev.wordpress.test.local.testcontainers.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress69BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    private static final WpRestClientFactory FACTORY = new OkHttpWpRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }

    @Override
    protected WpRestClientFactory clientFactory() {
        return FACTORY;
    }
}
