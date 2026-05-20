package io.github.evisentin.wordpress.client.adapter.okhttp.auth;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface OkHttpAuthenticationStrategyHandler<T extends WpAuthenticationStrategy> {

    default String authenticate(WpAuthenticationStrategy strategy) {
        return authenticateTyped(supports().cast(strategy));
    }

    String authenticateTyped(T strategy);

    default boolean canHandle(WpAuthenticationStrategy strategy) {
        return supports().isInstance(strategy);
    }

    Class<T> supports();
}
