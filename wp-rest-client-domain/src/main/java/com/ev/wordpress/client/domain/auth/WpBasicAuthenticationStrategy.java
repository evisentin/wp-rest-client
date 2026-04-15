package com.ev.wordpress.client.domain.auth;

import lombok.Getter;
import lombok.NonNull;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
public class WpBasicAuthenticationStrategy implements WpAuthenticationStrategy {

    private final String headerValue;

    public WpBasicAuthenticationStrategy(final @NonNull String userName, final @NonNull String password) {

        if (userName.isBlank())
            throw new IllegalArgumentException("userName must not be blank");

        if (password.isBlank())
            throw new IllegalArgumentException("password must not be blank");

        String combined = userName + ":" + password;

        this.headerValue = "Basic " + Base64.getEncoder().encodeToString(combined.getBytes(UTF_8));
    }
}
