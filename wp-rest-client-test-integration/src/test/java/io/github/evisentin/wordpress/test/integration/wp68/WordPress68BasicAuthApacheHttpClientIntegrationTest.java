package io.github.evisentin.wordpress.test.integration.wp68;

import io.github.evisentin.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpBasicAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress68BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    private static final WpBasicAuthRestClientFactory FACTORY = new ApacheWpBasicAuthRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.8";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
