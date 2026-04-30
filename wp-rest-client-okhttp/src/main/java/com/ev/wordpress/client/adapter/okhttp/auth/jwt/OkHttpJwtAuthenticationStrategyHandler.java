package com.ev.wordpress.client.adapter.okhttp.auth.jwt;

import com.ev.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.header.JwtAuthenticationHeaderProvider;
import lombok.NonNull;
import okhttp3.OkHttpClient;

public class OkHttpJwtAuthenticationStrategyHandler
        implements OkHttpAuthenticationStrategyHandler<WpJwtAuthenticationStrategy> {

    private final JwtAuthenticationHeaderProvider provider;

    public OkHttpJwtAuthenticationStrategyHandler(final @NonNull OkHttpClient authHttpClient) {
        this.provider = new JwtAuthenticationHeaderProvider(
                new OkHttpJwtTokenClient(authHttpClient)
        );
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
