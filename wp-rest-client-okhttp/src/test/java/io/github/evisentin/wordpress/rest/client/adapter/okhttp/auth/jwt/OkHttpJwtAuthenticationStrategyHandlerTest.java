package io.github.evisentin.wordpress.rest.client.adapter.okhttp.auth.jwt;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class OkHttpJwtAuthenticationStrategyHandlerTest {

    @Test
    void authenticateTyped_shouldFail_whenStrategyIsNull() {
        OkHttpJwtAuthenticationStrategyHandler handler =
                new OkHttpJwtAuthenticationStrategyHandler(
                        new OkHttpClient(),
                        "https://example.com/wp-json"
                );

        assertThatNullPointerException()
                .isThrownBy(() -> handler.authenticateTyped(null))
                .withMessage("strategy is marked non-null but is null");
    }

    @Test
    void constructor_shouldFail_whenApiUrlIsNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new OkHttpJwtAuthenticationStrategyHandler(
                        new OkHttpClient(),
                        null
                ))
                .withMessage("apiUrl is marked non-null but is null");
    }

    @Test
    void constructor_shouldFail_whenAuthHttpClientIsNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new OkHttpJwtAuthenticationStrategyHandler(
                        null,
                        "https://example.com/wp-json"
                ))
                .withMessage("authHttpClient is marked non-null but is null");
    }
}
