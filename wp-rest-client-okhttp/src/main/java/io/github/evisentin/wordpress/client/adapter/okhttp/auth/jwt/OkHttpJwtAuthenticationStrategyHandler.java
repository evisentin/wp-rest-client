package io.github.evisentin.wordpress.client.adapter.okhttp.auth.jwt;

import io.github.evisentin.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.header.JwtAuthenticationHeaderProvider;
import lombok.NonNull;
import okhttp3.OkHttpClient;

public class OkHttpJwtAuthenticationStrategyHandler
        implements OkHttpAuthenticationStrategyHandler<WpJwtAuthenticationStrategy> {

    private final JwtAuthenticationHeaderProvider provider;

    public OkHttpJwtAuthenticationStrategyHandler(final @NonNull OkHttpClient authHttpClient, final @NonNull String apiUrl) {
        this.provider = new JwtAuthenticationHeaderProvider(new OkHttpJwtTokenClient(authHttpClient, apiUrl));
    }

    @Override
    public String authenticateTyped(final @NonNull WpJwtAuthenticationStrategy strategy) {
        return provider.createAuthorizationHeader(strategy);
    }

    @Override
    public Class<WpJwtAuthenticationStrategy> supports() {
        return WpJwtAuthenticationStrategy.class;
    }
}
