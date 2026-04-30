package com.ev.wordpress.client.adapter.okhttp.auth.basic;

import com.ev.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.header.BasicAuthenticationHeaderProvider;
import lombok.NonNull;

public class OkHttpBasicAuthenticationStrategyHandler
        implements OkHttpAuthenticationStrategyHandler<WpBasicAuthenticationStrategy> {

    private final BasicAuthenticationHeaderProvider provider = new BasicAuthenticationHeaderProvider();

    @Override
    public String authenticateTyped(final @NonNull WpBasicAuthenticationStrategy strategy) {
        return provider.createAuthorizationHeader(strategy);
    }

    @Override
    public Class<WpBasicAuthenticationStrategy> supports() {
        return WpBasicAuthenticationStrategy.class;
    }
}
