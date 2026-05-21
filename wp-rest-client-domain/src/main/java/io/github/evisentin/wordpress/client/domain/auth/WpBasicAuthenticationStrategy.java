package io.github.evisentin.wordpress.client.domain.auth;

import lombok.NonNull;

public record WpBasicAuthenticationStrategy(@NonNull String username,
                                            @NonNull String password) implements WpAuthenticationStrategy {
    public WpBasicAuthenticationStrategy {
        if (username.isBlank()) throw new IllegalArgumentException("username must not be blank");
        if (password.isBlank()) throw new IllegalArgumentException("password must not be blank");
    }
}
