package io.github.evisentin.wordpress.test.integration.wp60;

import io.github.evisentin.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.ApacheWpBasicAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress60BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    private static final WpBasicAuthRestClientFactory FACTORY = new ApacheWpBasicAuthRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.0";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
