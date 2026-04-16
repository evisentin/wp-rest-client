package com.ev.wordpress.test.integration.wp65;

import com.ev.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.OkHttpWpRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress65BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    private static final WpRestClientFactory FACTORY = new OkHttpWpRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpRestClientFactory clientFactory() {
        return FACTORY;
    }
}
