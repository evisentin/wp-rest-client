package io.github.evisentin.wordpress.client.adapter.apache.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingResponseInterceptor implements HttpResponseInterceptor {

    private static final String PREFIX = "<<< ";

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {

        if (!log.isDebugEnabled()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n")
          .append(PREFIX).append("<<< HTTP RESPONSE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");

        sb.append(PREFIX)
          .append("Status: ")
          .append(response.getCode())
          .append("\n");

        for (Header header : response.getHeaders()) {
            sb.append(PREFIX)
              .append("[HEADER] ")
              .append(header.getName())
              .append(": ")
              .append(header.getValue())
              .append("\n");
        }

        if (response instanceof HttpEntityContainer container) {
            HttpEntity entityObj = container.getEntity();
            if (entityObj != null) {
                String body = EntityUtils.toString(entityObj, StandardCharsets.UTF_8);
                sb.append(PREFIX)
                  .append("Body: ")
                  .append(body)
                  .append("\n");

                ContentType contentType = null;
                String rawContentType = entityObj.getContentType();
                if (rawContentType != null) {
                    contentType = ContentType.parse(rawContentType);
                }

                container.setEntity(contentType != null
                        ? new StringEntity(body, contentType)
                        : new StringEntity(body, StandardCharsets.UTF_8));
            }
        }

        sb.append(PREFIX).append("====================");

        log.debug(sb.toString());
    }
}
