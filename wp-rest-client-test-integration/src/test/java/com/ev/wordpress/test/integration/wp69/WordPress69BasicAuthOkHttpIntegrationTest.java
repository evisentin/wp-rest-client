package com.ev.wordpress.test.integration.wp69;

import com.ev.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.OkHttpWpBasicAuthRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress69BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    private static final WpBasicAuthRestClientFactory FACTORY = new OkHttpWpBasicAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
