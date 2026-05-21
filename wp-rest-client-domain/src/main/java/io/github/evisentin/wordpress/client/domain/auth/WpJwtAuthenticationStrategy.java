package io.github.evisentin.wordpress.client.domain.auth;

import lombok.NonNull;

public record WpJwtAuthenticationStrategy(@NonNull String username,
                                          @NonNull String password,
                                          @NonNull String jwtTokenUrl) implements WpAuthenticationStrategy {
    public WpJwtAuthenticationStrategy {
        if (username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password.isBlank()) throw new IllegalArgumentException("password must not be blank");
        if (jwtTokenUrl.isBlank()) throw new IllegalArgumentException("jwtTokenUrl must not be blank");
    }
}
