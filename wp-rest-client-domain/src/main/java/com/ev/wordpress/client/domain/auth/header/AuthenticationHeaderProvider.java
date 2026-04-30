package com.ev.wordpress.client.domain.auth.header;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface AuthenticationHeaderProvider<T extends WpAuthenticationStrategy> {
    String createAuthorizationHeader(T strategy);
}
