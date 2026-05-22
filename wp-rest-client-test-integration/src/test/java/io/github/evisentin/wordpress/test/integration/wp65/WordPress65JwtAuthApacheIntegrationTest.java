package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.JwtAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpJwtAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpJwtAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress65JwtAuthApacheIntegrationTest extends JwtAuthWordPressIntegrationTest {
    private static final WpJwtAuthRestClientFactory FACTORY = new ApacheWpJwtAuthRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpJwtAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
