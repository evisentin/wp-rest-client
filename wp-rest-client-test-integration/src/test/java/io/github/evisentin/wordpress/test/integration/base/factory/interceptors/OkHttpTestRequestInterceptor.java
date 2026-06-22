package io.github.evisentin.wordpress.test.integration.base.factory.interceptors;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OkHttpTestRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        System.out.println("=== HTTP Request ===");
        System.out.println("URL: " + request.url());
        System.out.println("Method: " + request.method());
        System.out.println("Headers: " + request.headers());

        RequestBody body = request.body();
        if (body != null && isJson(body)) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);

            System.out.println("Body: " +
                               buffer.readString(StandardCharsets.UTF_8));
        }

        return chain.proceed(request);
    }

    private static boolean isJson(RequestBody body) {
        MediaType contentType = body.contentType();

        return contentType != null && ("json".equalsIgnoreCase(contentType.subtype()) || contentType.subtype().toLowerCase().endsWith("+json"));
    }
}
