package io.github.evisentin.wordpress.test.integration.wp70;

import io.github.evisentin.wordpress.test.integration.base.okhttp.BasicAuthOkHttpIntegrationTest;

public class WordPress70BasicAuthOkHttpIntegrationTest extends BasicAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "7.0";
    }
}
