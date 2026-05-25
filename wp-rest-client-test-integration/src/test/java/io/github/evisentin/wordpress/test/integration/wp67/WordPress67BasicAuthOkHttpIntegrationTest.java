package io.github.evisentin.wordpress.test.integration.wp67;

import io.github.evisentin.wordpress.test.integration.base.okhttp.BasicAuthOkHttpIntegrationTest;

public class WordPress67BasicAuthOkHttpIntegrationTest extends BasicAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.7";
    }
}
