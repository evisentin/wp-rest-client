package io.github.evisentin.wordpress.rest.client.samples.apache;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * Sample {@link HttpResponseInterceptor} that logs incoming HTTP responses.
 *
 * <p>This implementation is intentionally simple and intended for
 * demonstration purposes only. Production applications should use a logging framework such as SLF4J together with
 * Logback or Log4j.
 */
public class SampleHttpResponseInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(final HttpResponse response, final EntityDetails entity, final HttpContext context) {

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println("Incoming REST response");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        System.out.printf("Status : %d %s%n",
                response.getCode(),
                response.getReasonPhrase());

        System.out.println();
        System.out.println("Headers:");

        response.headerIterator().forEachRemaining(header ->
                System.out.printf("  %s: %s%n",
                        header.getName(),
                        header.getValue()));

        if (entity != null) {
            System.out.println();
            System.out.printf("Content-Type   : %s%n", entity.getContentType());
            System.out.printf("Content-Length : %d%n", entity.getContentLength());

            /*
             * Note:
             * HttpResponseInterceptor only has access to response metadata.
             * The response body is not available here.
             *
             * To log JSON payloads, intercept the response after the entity
             * has been read or use an Apache HttpClient ExecChainHandler.
             */
        }

        System.out.println("==================================================");
    }
}
