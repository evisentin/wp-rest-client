package com.ev.wordpress.client.domain.auth.providers;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface AuthenticationHeaderProvider<T extends WpAuthenticationStrategy> {
    String createAuthorizationHeader(T strategy);
}
