package io.github.evisentin.wordpress.test.integration.base.factory.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpTestRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        System.out.println("=== HTTP Request ===");
        System.out.println("URL: " + request.url());
        System.out.println("Method: " + request.method());
        System.out.println("Headers: " + request.headers());

        return chain.proceed(request);
    }
}
