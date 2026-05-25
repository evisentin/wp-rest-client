package io.github.evisentin.wordpress.test.integration.wp66;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress66BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.6";
    }
}
