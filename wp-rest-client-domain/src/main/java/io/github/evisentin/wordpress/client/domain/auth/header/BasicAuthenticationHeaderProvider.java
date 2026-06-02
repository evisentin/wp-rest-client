package io.github.evisentin.wordpress.client.domain.auth.header;

import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Creates HTTP Basic Authentication {@code Authorization} headers.
 *
 * <p>The username and password are joined as {@code username:password}, UTF-8 encoded, Base64 encoded, and prefixed
 * with {@code Basic}.</p>
 */
public final class BasicAuthenticationHeaderProvider
        implements AuthenticationHeaderProvider<WpBasicAuthenticationStrategy> {

    @Override
    public String createAuthorizationHeader(WpBasicAuthenticationStrategy strategy) {
        String combined = strategy.username() + ":" + strategy.password();
        return "Basic " + Base64.getEncoder()
                                .encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }
}
