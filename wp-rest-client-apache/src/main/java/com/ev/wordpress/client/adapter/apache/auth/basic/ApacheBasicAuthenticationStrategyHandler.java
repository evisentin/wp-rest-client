package com.ev.wordpress.client.adapter.apache.auth.basic;

import com.ev.wordpress.client.adapter.apache.auth.ApacheAuthenticationStrategyHandler;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.header.BasicAuthenticationHeaderProvider;
import lombok.NonNull;

public class ApacheBasicAuthenticationStrategyHandler
        implements ApacheAuthenticationStrategyHandler<WpBasicAuthenticationStrategy> {

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
