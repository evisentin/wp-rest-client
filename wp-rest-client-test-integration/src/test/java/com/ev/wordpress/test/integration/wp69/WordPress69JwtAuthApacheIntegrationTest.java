package com.ev.wordpress.test.integration.wp69;

import com.ev.wordpress.test.integration.base.JwtAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.OkHttpWpJwtAuthRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress69JwtAuthApacheIntegrationTest extends JwtAuthWordPressIntegrationTest {
    private static final WpJwtAuthRestClientFactory FACTORY = new OkHttpWpJwtAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }

    @Override
    protected WpJwtAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
