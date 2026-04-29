package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandlerRegistry;
import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.AUTHORIZATION;

public class AuthenticationInterceptor implements Interceptor {

    private final OkHttpAuthenticationStrategyHandlerRegistry registry = new OkHttpAuthenticationStrategyHandlerRegistry();
    private final WpAuthenticationStrategy strategy;

    public AuthenticationInterceptor(final @NonNull WpAuthenticationStrategy strategy) {
        this.strategy = strategy;
    }

    @NotNull
    @Override
    public Response intercept(final @NonNull Chain chain) throws IOException {
        final Request original = chain.request();

        final String value = registry.getHandler(strategy).authenticate(strategy);

        return chain.proceed(original.newBuilder()
                                     .header(AUTHORIZATION, value)
                                     .method(original.method(), original.body())
                                     .build());
    }
}
