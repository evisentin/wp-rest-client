package io.github.evisentin.wordpress.test.integration.wp63;

import io.github.evisentin.wordpress.test.integration.base.okhttp.JwtAuthOkHttpIntegrationTest;

public class WordPress63JwtAuthOkHttpIntegrationTest extends JwtAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.3";
    }
}
