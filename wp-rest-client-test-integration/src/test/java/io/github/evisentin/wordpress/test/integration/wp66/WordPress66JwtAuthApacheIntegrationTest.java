package io.github.evisentin.wordpress.test.integration.wp66;

import io.github.evisentin.wordpress.test.integration.base.apache.JwtAuthApacheIntegrationTest;

public class WordPress66JwtAuthApacheIntegrationTest extends JwtAuthApacheIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.6";
    }
}
