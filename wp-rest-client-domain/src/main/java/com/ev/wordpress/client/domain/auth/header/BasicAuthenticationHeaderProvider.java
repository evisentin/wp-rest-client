package com.ev.wordpress.client.domain.auth.header;

import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class BasicAuthenticationHeaderProvider
        implements AuthenticationHeaderProvider<WpBasicAuthenticationStrategy> {

    @Override
    public String createAuthorizationHeader(WpBasicAuthenticationStrategy strategy) {
        String combined = strategy.username() + ":" + strategy.password();
        return "Basic " + Base64.getEncoder()
                                .encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }
}
