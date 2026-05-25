package io.github.evisentin.wordpress.test.integration.wp67;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress67BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.7";
    }
}
