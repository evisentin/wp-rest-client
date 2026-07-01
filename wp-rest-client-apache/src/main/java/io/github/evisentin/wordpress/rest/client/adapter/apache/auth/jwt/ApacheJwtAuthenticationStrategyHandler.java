package io.github.evisentin.wordpress.rest.client.adapter.apache.auth.jwt;

import io.github.evisentin.wordpress.rest.client.adapter.apache.auth.ApacheAuthenticationStrategyHandler;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.header.JwtAuthenticationHeaderProvider;
import lombok.NonNull;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

public class ApacheJwtAuthenticationStrategyHandler
        implements ApacheAuthenticationStrategyHandler<WpJwtAuthenticationStrategy> {

    private final JwtAuthenticationHeaderProvider provider;

    public ApacheJwtAuthenticationStrategyHandler(final @NonNull CloseableHttpClient authHttpClient, final @NonNull String apiUrl) {
        this.provider = new JwtAuthenticationHeaderProvider(new ApacheJwtTokenClient(authHttpClient, apiUrl));
    }

    @Override
    public String authenticateTyped(final @NonNull WpJwtAuthenticationStrategy strategy) {
        return provider.createAuthorizationHeader(strategy);
    }

    @Override
    public Class<WpJwtAuthenticationStrategy> supports() {
        return WpJwtAuthenticationStrategy.class;
    }
}
