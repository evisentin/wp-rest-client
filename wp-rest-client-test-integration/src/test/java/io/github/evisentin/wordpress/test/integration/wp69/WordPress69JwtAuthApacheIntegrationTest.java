package io.github.evisentin.wordpress.test.integration.wp69;

import io.github.evisentin.wordpress.test.integration.base.apache.JwtAuthApacheIntegrationTest;

public class WordPress69JwtAuthApacheIntegrationTest extends JwtAuthApacheIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }
}
