package io.github.evisentin.wordpress.test.integration.wp64;

import io.github.evisentin.wordpress.test.integration.base.JwtAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.OkHttpWpJwtAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress64JwtAuthOkHttpIntegrationTest extends JwtAuthWordPressIntegrationTest {
    private static final WpJwtAuthRestClientFactory FACTORY = new OkHttpWpJwtAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.4";
    }

    @Override
    protected WpJwtAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
