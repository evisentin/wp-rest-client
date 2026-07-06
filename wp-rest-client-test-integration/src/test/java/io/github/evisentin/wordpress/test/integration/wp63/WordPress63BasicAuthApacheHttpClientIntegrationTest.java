package io.github.evisentin.wordpress.test.integration.wp63;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress63BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.3";
    }
}
