package com.ev.wordpress.test.local.testcontainers.wp69;

import com.ev.wordpress.test.local.testcontainers.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.local.testcontainers.base.factory.ApacheWpRestClientFactory;
import com.ev.wordpress.test.local.testcontainers.base.factory.WpRestClientFactory;

import static com.ev.wordpress.test.local.testcontainers.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress69BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    private static final WpRestClientFactory FACTORY = new ApacheWpRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }

    @Override
    protected WpRestClientFactory clientFactory() {
        return FACTORY;
    }
}
