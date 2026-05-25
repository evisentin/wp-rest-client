package io.github.evisentin.wordpress.test.integration.wp65;

import io.github.evisentin.wordpress.test.integration.base.okhttp.BasicAuthOkHttpIntegrationTest;

public class WordPress65BasicAuthOkHttpIntegrationTest extends BasicAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.5";
    }
}
