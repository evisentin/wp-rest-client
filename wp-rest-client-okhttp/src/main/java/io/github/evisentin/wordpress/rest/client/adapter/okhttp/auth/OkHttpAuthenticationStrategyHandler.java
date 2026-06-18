package io.github.evisentin.wordpress.rest.client.adapter.okhttp.auth;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthenticationStrategy;

public interface OkHttpAuthenticationStrategyHandler<T extends WpAuthenticationStrategy> {

    default String authenticate(final WpAuthenticationStrategy strategy) {
        return authenticateTyped(supports().cast(strategy));
    }

    String authenticateTyped(T strategy);

    default boolean canHandle(final WpAuthenticationStrategy strategy) {
        return supports().isInstance(strategy);
    }

    Class<T> supports();
}
