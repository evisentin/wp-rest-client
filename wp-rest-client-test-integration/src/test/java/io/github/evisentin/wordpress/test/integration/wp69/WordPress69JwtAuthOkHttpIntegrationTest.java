package io.github.evisentin.wordpress.test.integration.wp69;

import io.github.evisentin.wordpress.test.integration.base.okhttp.JwtAuthOkHttpIntegrationTest;

public class WordPress69JwtAuthOkHttpIntegrationTest extends JwtAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.9";
    }
}
