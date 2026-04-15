package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {

    private final WpAuthenticationStrategy strategy;

    public AuthenticationInterceptor(final @NonNull WpAuthenticationStrategy strategy) {
        this.strategy = strategy;
    }

    @NotNull
    @Override
    public Response intercept(final @NonNull Chain chain) throws IOException {
        final Request original = chain.request();

        return chain.proceed(original.newBuilder()
                                     .header(strategy.getHeaderName(), strategy.getHeaderValue())
                                     .method(original.method(), original.body())
                                     .build());
    }
}
