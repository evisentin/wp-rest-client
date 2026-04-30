package com.ev.wordpress.client.adapter.apache.auth;

import com.ev.wordpress.client.adapter.apache.auth.basic.ApacheBasicAuthenticationStrategyHandler;
import com.ev.wordpress.client.adapter.apache.auth.jwt.ApacheJwtAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.util.HashSet;
import java.util.Set;

public class ApacheAuthenticationStrategyHandlerRegistry {

    private final Set<ApacheAuthenticationStrategyHandler<?>> handlers = new HashSet<>();

    public ApacheAuthenticationStrategyHandlerRegistry(final @NonNull CloseableHttpClient authHttpClient) {
        handlers.add(new ApacheBasicAuthenticationStrategyHandler());
        handlers.add(new ApacheJwtAuthenticationStrategyHandler(authHttpClient));
    }

    public ApacheAuthenticationStrategyHandler<?> getHandler(final @NonNull WpAuthenticationStrategy strategy) {
        return handlers.stream()
                       .filter(h -> h.canHandle(strategy))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("No handler found"));
    }
}
