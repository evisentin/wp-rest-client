package com.ev.wordpress.test.integration.wp65;

import com.ev.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.ApacheWpRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

public class WordPress65BasicAuthApacheHttpClientIntegrationTest extends BasicAuthWordPressIntegrationTest {

    private static final WpRestClientFactory FACTORY = new ApacheWpRestClientFactory(insecureForApache());

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }

    @Override
    protected WpRestClientFactory clientFactory() {
        return FACTORY;
    }
}
