package com.ev.wordpress.client.adapter.apache.auth;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ApacheAuthenticationStrategyHandlerRegistry {

    private final Set<ApacheAuthenticationStrategyHandler<?>> handlers = new HashSet<>();

    public ApacheAuthenticationStrategyHandlerRegistry() {
        handlers.add(new ApacheBasicAuthenticationStrategyHandler());
    }

    public ApacheAuthenticationStrategyHandler<?> getHandler(final @NonNull WpAuthenticationStrategy strategy) {
        return handlers.stream()
                       .filter(h -> h.canHandle(strategy))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("No handler found"));
    }
}
