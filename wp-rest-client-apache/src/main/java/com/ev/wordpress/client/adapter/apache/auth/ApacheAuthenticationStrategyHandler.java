package com.ev.wordpress.client.adapter.apache.auth;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface ApacheAuthenticationStrategyHandler<T extends WpAuthenticationStrategy> {

    default String authenticate(WpAuthenticationStrategy strategy) {
        return authenticateTyped(supports().cast(strategy));
    }

    String authenticateTyped(T strategy);

    default boolean canHandle(WpAuthenticationStrategy strategy) {
        return supports().isInstance(strategy);
    }

    Class<T> supports();
}
