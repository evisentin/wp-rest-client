package com.ev.wordpress.client.adapter.apache.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

public class AuthenticationInterceptor implements HttpRequestInterceptor {

    private final WpAuthenticationStrategy strategy;

    public AuthenticationInterceptor(final @NonNull WpAuthenticationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void process(final HttpRequest request,
                        final EntityDetails entity,
                        final HttpContext context) {
        request.addHeader(strategy.getHeaderName(), strategy.getHeaderValue());
    }
}
