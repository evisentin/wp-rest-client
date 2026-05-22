package io.github.evisentin.wordpress.test.integration.wp64;

import io.github.evisentin.wordpress.test.integration.base.JwtAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpJwtAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress64JwtAuthApacheIntegrationTest extends JwtAuthWordPressIntegrationTest {
    private static final WpJwtAuthRestClientFactory FACTORY = new ApacheWpJwtAuthRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.4";
    }

    @Override
    protected WpJwtAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
