package io.github.evisentin.wordpress.client.adapter.okhttp.auth;

import io.github.evisentin.wordpress.client.adapter.okhttp.auth.basic.OkHttpBasicAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.adapter.okhttp.auth.jwt.OkHttpJwtAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import okhttp3.OkHttpClient;

import java.util.HashSet;
import java.util.Set;

public class OkHttpAuthenticationStrategyHandlerRegistry {

    private final Set<OkHttpAuthenticationStrategyHandler<?>> handlers = new HashSet<>();

    public OkHttpAuthenticationStrategyHandlerRegistry(final @NonNull OkHttpClient authHttpClient) {
        handlers.add(new OkHttpBasicAuthenticationStrategyHandler());
        handlers.add(new OkHttpJwtAuthenticationStrategyHandler(authHttpClient));
    }

    public OkHttpAuthenticationStrategyHandler<?> getHandler(final @NonNull WpAuthenticationStrategy strategy) {
        return handlers.stream()
                       .filter(h -> h.canHandle(strategy))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("No handler found"));
    }
}
