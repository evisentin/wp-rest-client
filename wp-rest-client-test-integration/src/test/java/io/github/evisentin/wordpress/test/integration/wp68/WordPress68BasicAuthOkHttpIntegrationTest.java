package io.github.evisentin.wordpress.test.integration.wp68;

import io.github.evisentin.wordpress.test.integration.base.okhttp.BasicAuthOkHttpIntegrationTest;

public class WordPress68BasicAuthOkHttpIntegrationTest extends BasicAuthOkHttpIntegrationTest {

    @Override
    public String getWordPressVersion() {
        return "6.8";
    }
}
