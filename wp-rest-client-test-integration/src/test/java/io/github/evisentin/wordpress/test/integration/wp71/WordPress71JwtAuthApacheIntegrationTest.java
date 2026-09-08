package io.github.evisentin.wordpress.test.integration.wp71;

import io.github.evisentin.wordpress.test.integration.base.apache.JwtAuthApacheIntegrationTest;

public class WordPress71JwtAuthApacheIntegrationTest extends JwtAuthApacheIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "7.1";
    }
}
