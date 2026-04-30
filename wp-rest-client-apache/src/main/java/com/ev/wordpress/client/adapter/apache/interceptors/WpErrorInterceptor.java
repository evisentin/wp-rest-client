package com.ev.wordpress.client.adapter.apache.interceptors;

import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.client.domain.exception.WpForbiddenException;
import com.ev.wordpress.client.domain.exception.WpNotFoundException;
import com.ev.wordpress.client.domain.exception.WpUnauthorizedException;
import com.ev.wordpress.client.domain.model.WpError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class WpErrorInterceptor implements HttpResponseInterceptor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void process(HttpResponse response, EntityDetails entityDetails, HttpContext context)
            throws HttpException, IOException {

        int statusCode = response.getCode();

        // success → do nothing
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }

        HttpEntity entity = null;
        if (response instanceof ClassicHttpResponse classicResponse) {
            entity = classicResponse.getEntity();
        }

        String rawBody = entity != null ? EntityUtils.toString(entity) : null;

        WpError wpError = parseWpError(rawBody);

        // ⚠️ IMPORTANT: entity is consumed → rebuild it
        if (response instanceof ClassicHttpResponse classicResponse) {
            classicResponse.setEntity(
                    new org.apache.hc.core5.http.io.entity.StringEntity(rawBody == null ? "" : rawBody)
            );
        }

        throw mapException(statusCode, wpError, rawBody);
    }

    private static String buildBodySuffix(String rawBody) {
        return isBlank(rawBody) ? "" : ", body=" + rawBody;
    }

    private static RuntimeException mapException(int statusCode, WpError wpError, String rawBody) {
        return switch (statusCode) {
            case 400 -> new WpBadRequestException(wpError);
            case 401 -> new WpUnauthorizedException(wpError);
            case 403 -> new WpForbiddenException(wpError);
            case 404 -> new WpNotFoundException(wpError);
            default -> new RuntimeException(
                    "Unexpected HTTP status " + statusCode + buildBodySuffix(rawBody)
            );
        };
    }

    private static WpError parseWpError(String rawBody) {
        if (isBlank(rawBody)) return null;

        try {
            return MAPPER.readValue(rawBody, WpError.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
