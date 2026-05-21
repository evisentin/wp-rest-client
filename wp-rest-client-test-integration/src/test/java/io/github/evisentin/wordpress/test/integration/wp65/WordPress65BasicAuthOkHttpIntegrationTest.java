package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.factory.OkHttpWpBasicAuthRestClientFactory;
import io.github.evisentin.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static io.github.evisentin.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForOkHttp;

public class WordPress65BasicAuthOkHttpIntegrationTest extends BasicAuthWordPressIntegrationTest {
    private static final WpBasicAuthRestClientFactory FACTORY = new OkHttpWpBasicAuthRestClientFactory(insecureForOkHttp());

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpBasicAuthRestClientFactory clientFactory() {
        return FACTORY;
    }
}
