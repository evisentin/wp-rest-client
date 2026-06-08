package io.github.evisentin.wordpress.client.adapter.apache.auth;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface ApacheAuthenticationStrategyHandler<T extends WpAuthenticationStrategy> {

    default String authenticate(final WpAuthenticationStrategy strategy) {
        return authenticateTyped(supports().cast(strategy));
    }

    String authenticateTyped(T strategy);

    default boolean canHandle(final WpAuthenticationStrategy strategy) {
        return supports().isInstance(strategy);
    }

    Class<T> supports();
}
