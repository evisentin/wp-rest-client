package io.github.evisentin.wordpress.test.integration.base.factory.interceptors;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class OkHttpTestResponseInterceptor implements Interceptor {

    private static final long MAX_BODY_SIZE = 1024 * 1024; // 1 MB

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        System.out.println("=== HTTP Response ===");
        System.out.println("URL: " + response.request().url());
        System.out.println("Code: " + response.code());
        System.out.println("Message: " + response.message());
        System.out.println("Headers: " + response.headers());

        ResponseBody body = response.body();
        if (body != null) {
            MediaType contentType = body.contentType();

            if (contentType != null &&
                "application".equalsIgnoreCase(contentType.type()) &&
                contentType.subtype().toLowerCase().contains("json")) {

                String json = response.peekBody(MAX_BODY_SIZE).string();

                System.out.println("JSON Response:");
                System.out.println(json);
            }
        }

        return response;
    }
}
