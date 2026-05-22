package io.github.evisentin.wordpress.test.integration.wp60;

import io.github.evisentin.wordpress.test.integration.base.JwtAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpJwtAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress60JwtAuthOkHttpIntegrationTest extends JwtAuthWordPressIntegrationTest {
    private static final WpJwtAuthRestClientFactory FACTORY = new ApacheWpJwtAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.0";
    }

    @Override
    protected WpJwtAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
