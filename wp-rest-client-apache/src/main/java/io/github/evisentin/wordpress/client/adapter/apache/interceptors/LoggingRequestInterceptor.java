package io.github.evisentin.wordpress.client.adapter.apache.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingRequestInterceptor implements HttpRequestInterceptor {

    private static final String PREFIX = ">>> ";

    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {

        if (!log.isDebugEnabled()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("\n")
          .append(PREFIX)
          .append(">>> HTTP REQUEST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");

        sb.append(PREFIX)
          .append(request.getMethod())
          .append(" ")
          .append(request.getRequestUri())
          .append("\n");

        for (Header header : request.getHeaders()) {
            sb.append(PREFIX)
              .append("[HEADER] ")
              .append(header.getName())
              .append(": ")
              .append(header.getValue())
              .append("\n");
        }

        if (request instanceof HttpEntityContainer container) {

            HttpEntity entityObj = container.getEntity();

            if (entityObj != null) {

                ContentType contentType = null;

                String rawContentType = entityObj.getContentType();

                if (rawContentType != null) {
                    contentType = ContentType.parse(rawContentType);
                }

                boolean multipart =
                        contentType != null
                        && contentType.getMimeType().startsWith("multipart/");

                if (multipart) {

                    sb.append(PREFIX)
                      .append("Body: <multipart upload omitted>")
                      .append("\n");

                    sb.append(PREFIX)
                      .append("Content-Length: ")
                      .append(entityObj.getContentLength())
                      .append("\n");
                } else {

                    String body;

                    try {
                        body = EntityUtils.toString(entityObj, StandardCharsets.UTF_8);
                    } catch (ContentTooLongException ex) {
                        body = "<too long to render>";
                    }

                    sb.append(PREFIX)
                      .append("Body: ")
                      .append(body)
                      .append("\n");

                    container.setEntity(contentType != null
                            ? new StringEntity(body, contentType)
                            : new StringEntity(body, StandardCharsets.UTF_8));
                }
            }
        }

        log.debug(sb.toString());
    }
}
