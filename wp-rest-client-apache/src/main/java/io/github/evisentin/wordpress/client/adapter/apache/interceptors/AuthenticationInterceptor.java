package io.github.evisentin.wordpress.client.adapter.apache.interceptors;

import io.github.evisentin.wordpress.client.adapter.apache.auth.ApacheAuthenticationStrategyHandlerRegistry;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

import static org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationInterceptor implements HttpRequestInterceptor {

    private final WpAuthenticationStrategy strategy;
    private final ApacheAuthenticationStrategyHandlerRegistry registry;

    public AuthenticationInterceptor(final @NonNull WpAuthenticationStrategy strategy,
                                     final @NonNull CloseableHttpClient authHttpClient) {
        this.strategy = strategy;
        registry = new ApacheAuthenticationStrategyHandlerRegistry(authHttpClient);
    }

    @Override
    public void process(final HttpRequest request,
                        final EntityDetails entity,
                        final HttpContext context) {

        final String value = registry.getHandler(strategy).authenticate(strategy);

        request.addHeader(AUTHORIZATION, value);
    }
}
