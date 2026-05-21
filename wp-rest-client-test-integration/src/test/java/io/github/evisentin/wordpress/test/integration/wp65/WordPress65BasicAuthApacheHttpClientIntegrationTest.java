package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpBasicAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress65BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    private static final WpBasicAuthRestClientFactory FACTORY = new ApacheWpBasicAuthRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
