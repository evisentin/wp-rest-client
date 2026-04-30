package com.ev.wordpress.test.integration.wp68;

import com.ev.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import com.ev.wordpress.test.integration.base.factory.ApacheWpBasicAuthRestClientFactory;
import com.ev.wordpress.test.integration.base.factory.WpBasicAuthRestClientFactory;

import static com.ev.wordpress.test.integration.base.factory.TestSslConfigurations.insecureForApache;

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
