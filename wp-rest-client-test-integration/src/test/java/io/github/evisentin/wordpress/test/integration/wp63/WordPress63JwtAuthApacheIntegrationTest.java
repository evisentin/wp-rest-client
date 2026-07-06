package io.github.evisentin.wordpress.test.integration.wp63;

import io.github.evisentin.wordpress.test.integration.base.apache.JwtAuthApacheIntegrationTest;

public class WordPress63JwtAuthApacheIntegrationTest extends JwtAuthApacheIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.3";
    }
}
