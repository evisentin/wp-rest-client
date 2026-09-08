package io.github.evisentin.wordpress.test.integration.wp71;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress71BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "7.1";
    }
}
