package io.github.evisentin.wordpress.test.integration.wp71;

import io.github.evisentin.wordpress.test.integration.base.okhttp.BasicAuthOkHttpIntegrationTest;

public class WordPress71BasicAuthOkHttpIntegrationTest extends BasicAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "7.1";
    }
}
