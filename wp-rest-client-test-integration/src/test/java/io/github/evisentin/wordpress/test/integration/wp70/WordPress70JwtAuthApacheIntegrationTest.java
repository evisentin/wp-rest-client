package io.github.evisentin.wordpress.test.integration.wp70;

import io.github.evisentin.wordpress.test.integration.base.apache.JwtAuthApacheIntegrationTest;

public class WordPress70JwtAuthApacheIntegrationTest extends JwtAuthApacheIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "7.0";
    }
}
