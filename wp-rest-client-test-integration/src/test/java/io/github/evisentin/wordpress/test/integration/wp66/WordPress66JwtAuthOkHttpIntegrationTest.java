package io.github.evisentin.wordpress.test.integration.wp66;

import io.github.evisentin.wordpress.test.integration.base.okhttp.JwtAuthOkHttpIntegrationTest;

public class WordPress66JwtAuthOkHttpIntegrationTest extends JwtAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.6";
    }
}
