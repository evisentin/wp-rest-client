package io.github.evisentin.wordpress.client.domain.auth.header;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;

public interface AuthenticationHeaderProvider<T extends WpAuthenticationStrategy> {
    String createAuthorizationHeader(T strategy);
}
