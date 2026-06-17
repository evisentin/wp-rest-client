package io.github.evisentin.wordpress.rest.client.samples.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * Sample {@link Interceptor} that logs incoming HTTP responses.
 *
 * <p>This implementation is intentionally simple and intended for demonstration purposes only. Production applications
 * should use a logging framework such as SLF4J together with Logback or Log4j.</p>
 */
public class SampleHttpResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(final Chain chain) throws IOException {

        final Response response = chain.proceed(chain.request());

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Incoming REST response");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        System.out.printf("Status : %d %s%n",
                response.code(),
                response.message());

        System.out.println();
        System.out.println("Headers:");

        response.headers().forEach(header ->
                System.out.printf("  %s: %s%n",
                        header.getFirst(),
                        header.getSecond()));

        final ResponseBody responseBody = response.body();

        if (responseBody != null) {

            System.out.println();
            System.out.printf("Content-Type   : %s%n", responseBody.contentType());
            System.out.printf("Content-Length : %d%n", responseBody.contentLength());

            final MediaType mediaType = responseBody.contentType();

            if (mediaType != null
                && "application".equalsIgnoreCase(mediaType.type())
                && mediaType.subtype().toLowerCase().contains("json")) {

                final String body = responseBody.string();

                ObjectMapper objectMapper = new ObjectMapper();

                String json = objectMapper.writerWithDefaultPrettyPrinter()
                                          .writeValueAsString(objectMapper.readTree(body));

                System.out.println();
                System.out.println("JSON Response:");
                System.out.println(json);

                // Re-create the response body so downstream code can still read it.
                return response.newBuilder()
                               .body(ResponseBody.create(body, mediaType))
                               .build();
            }
        }

        System.out.println("==================================================");
        System.out.println();

        return response;
    }
}
