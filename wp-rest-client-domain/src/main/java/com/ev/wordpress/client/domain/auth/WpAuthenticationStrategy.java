package com.ev.wordpress.client.domain.auth;

public interface WpAuthenticationStrategy {
    default String getHeaderName() {
        return "Authorization";
    }

    String getHeaderValue();
}
