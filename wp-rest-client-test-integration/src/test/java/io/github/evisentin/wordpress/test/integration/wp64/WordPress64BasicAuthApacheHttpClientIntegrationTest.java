package io.github.evisentin.wordpress.test.integration.wp64;

import io.github.evisentin.wordpress.test.integration.base.apache.BasicAuthApacheHttpClientIntegrationTest;

public class WordPress64BasicAuthApacheHttpClientIntegrationTest extends BasicAuthApacheHttpClientIntegrationTest {
    @Override
    public String getWordPressVersion() {
        return "6.4";
    }
}
