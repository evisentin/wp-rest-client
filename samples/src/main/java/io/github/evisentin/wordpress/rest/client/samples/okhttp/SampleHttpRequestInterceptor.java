package io.github.evisentin.wordpress.rest.client.samples.okhttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Sample {@link Interceptor} that logs outgoing HTTP requests.
 *
 * <p>This implementation is intentionally simple and intended for demonstration purposes only. Production applications
 * should use a logging framework such as SLF4J together with Logback or Log4j.</p>
 */
public class SampleHttpRequestInterceptor implements Interceptor {

    @Override
    public Response intercept(final Chain chain) throws IOException {

        final Request request = chain.request();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("Outgoing REST request");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        System.out.printf("Method : %s%n", request.method());
        System.out.printf("URL    : %s%n", request.url());

        System.out.println();
        System.out.println("Headers:");

        for (String name : request.headers().names()) {
            System.out.printf("  %s: %s%n",
                    name,
                    request.header(name));
        }

        if (request.body() != null) {
            System.out.println();
            System.out.printf("Content-Type   : %s%n", request.body().contentType());
            System.out.printf("Content-Length : %d%n", request.body().contentLength());
        }

        System.out.println("==================================================");
        System.out.println();

        return chain.proceed(request);
    }
}
