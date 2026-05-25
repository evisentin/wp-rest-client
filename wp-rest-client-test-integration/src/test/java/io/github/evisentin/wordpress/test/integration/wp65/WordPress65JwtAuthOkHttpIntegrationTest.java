package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.okhttp.JwtAuthOkHttpIntegrationTest;

public class WordPress65JwtAuthOkHttpIntegrationTest extends JwtAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }
}
