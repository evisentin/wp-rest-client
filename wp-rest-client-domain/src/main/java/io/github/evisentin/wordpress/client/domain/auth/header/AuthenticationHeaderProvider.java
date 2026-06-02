package io.github.evisentin.wordpress.client.domain.auth.header;

import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;

/**
 * Creates HTTP {@code Authorization} header values from authentication strategies.
 *
 * @param <T>
 *         supported authentication strategy type
 */
public interface AuthenticationHeaderProvider<T extends WpAuthenticationStrategy> {

    /**
     * Creates the value of an HTTP {@code Authorization} header.
     *
     * @param strategy
     *         authentication strategy to use
     *
     * @return authorization header value
     */
    String createAuthorizationHeader(T strategy);
}
