package io.github.evisentin.wordpress.test.integration.wp67;

import io.github.evisentin.wordpress.test.integration.base.okhttp.JwtAuthOkHttpIntegrationTest;

public class WordPress67JwtAuthOkHttpIntegrationTest extends JwtAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.7";
    }
}
