package com.ev.wordpress.client.adapter.apache.auth;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import lombok.NonNull;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ApacheBasicAuthenticationStrategyHandler implements ApacheAuthenticationStrategyHandler<WpBasicAuthenticationStrategy> {
    @Override
    public String authenticateTyped(final @NonNull WpBasicAuthenticationStrategy strategy) {
        String combined = strategy.username() + ":" + strategy.password();

        return "Basic " + Base64.getEncoder()
                                .encodeToString(combined.getBytes(UTF_8));
    }

    @Override
    public boolean canHandle(final @NonNull WpAuthenticationStrategy strategy) {
        return strategy instanceof WpBasicAuthenticationStrategy;
    }

    @Override
    public Class<WpBasicAuthenticationStrategy> supports() {
        return WpBasicAuthenticationStrategy.class;
    }
}
