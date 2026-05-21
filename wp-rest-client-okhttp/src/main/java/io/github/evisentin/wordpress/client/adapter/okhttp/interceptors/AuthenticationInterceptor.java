package io.github.evisentin.wordpress.client.adapter.okhttp.interceptors;

import io.github.evisentin.wordpress.client.adapter.okhttp.auth.OkHttpAuthenticationStrategyHandlerRegistry;
import io.github.evisentin.wordpress.client.domain.auth.WpAuthenticationStrategy;
import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationInterceptor implements Interceptor {

    private final OkHttpAuthenticationStrategyHandlerRegistry registry;
    private final WpAuthenticationStrategy strategy;

    public AuthenticationInterceptor(final @NonNull WpAuthenticationStrategy strategy,
                                     final @NonNull OkHttpClient authHttpClient) {

        this.strategy = strategy;
        registry = new OkHttpAuthenticationStrategyHandlerRegistry(authHttpClient);
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
