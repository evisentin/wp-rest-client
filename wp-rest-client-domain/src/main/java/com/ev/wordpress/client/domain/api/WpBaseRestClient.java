package com.ev.wordpress.client.domain.api;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class WpBaseRestClient implements WpRestClient {
    protected final String baseUrl;
    protected final WpAuthenticationStrategy authenticationStrategy;
}
