package com.ev.wordpress.client.adapter.okhttp.auth;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class OkHttpAuthenticationStrategyHandlerRegistry {

    private final Set<OkHttpAuthenticationStrategyHandler<?>> handlers = new HashSet<>();

    public OkHttpAuthenticationStrategyHandlerRegistry() {
        handlers.add(new OkHttpBasicAuthenticationStrategyHandler());
    }

    public OkHttpAuthenticationStrategyHandler<?> getHandler(final @NonNull WpAuthenticationStrategy strategy) {
        return handlers.stream()
                       .filter(h -> h.canHandle(strategy))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("No handler found"));
    }
}
