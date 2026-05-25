package io.github.evisentin.wordpress.test.integration.wp69;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress69BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }
}
