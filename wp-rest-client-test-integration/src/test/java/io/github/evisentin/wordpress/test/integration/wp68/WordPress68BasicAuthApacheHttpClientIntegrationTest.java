package io.github.evisentin.wordpress.test.integration.wp68;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress68BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.8";
    }
}
