package com.ev.wordpress.test.integration.wp67;

import com.ev.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.OkHttpWpBasicAuthRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress67BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    private static final WpBasicAuthRestClientFactory FACTORY = new OkHttpWpBasicAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.7";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
