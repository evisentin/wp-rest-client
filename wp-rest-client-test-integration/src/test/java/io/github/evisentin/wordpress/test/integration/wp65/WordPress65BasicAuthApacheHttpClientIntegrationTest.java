package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress65BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.5";
    }
}
