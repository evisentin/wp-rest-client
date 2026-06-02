package io.github.evisentin.wordpress.client.adapter.okhttp.auth;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;

public interface OkHttpAuthenticationStrategyHandler<T extends WpAuthenticationStrategy> {

    default String authenticate(final @NonNull WpAuthenticationStrategy strategy) {
        return authenticateTyped(supports().cast(strategy));
    }

    String authenticateTyped(T strategy);

    default boolean canHandle(final @NonNull WpAuthenticationStrategy strategy) {
        return supports().isInstance(strategy);
    }

    Class<T> supports();
}
