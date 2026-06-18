package io.github.evisentin.wordpress.rest.client.samples.apache;

import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * Sample {@link HttpRequestInterceptor} that logs outgoing HTTP requests.
 *
 * <p>This implementation is intentionally simple and intended for
 * demonstration purposes only. Production applications should use a logging framework such as SLF4J together with
 * Logback or Log4j.
 */
public class SampleHttpRequestInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(final HttpRequest request, final EntityDetails entity, final HttpContext context) {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("Outgoing REST request");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        System.out.printf("Method : %s%n", request.getMethod());
        System.out.printf("URI    : %s%n", request.getRequestUri());

        System.out.println();
        System.out.println("Headers:");

        request.headerIterator().forEachRemaining(header ->
                System.out.printf("  %s: %s%n",
                        header.getName(),
                        header.getValue()));

        if (entity != null) {
            System.out.println();
            System.out.printf("Content-Type   : %s%n", entity.getContentType());
            System.out.printf("Content-Length : %d%n", entity.getContentLength());
        }

        System.out.println("==================================================");
        System.out.println();
    }
}
