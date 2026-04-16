package com.ev.wordpress.client.adapter.okhttp.interceptors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingInterceptor implements Interceptor {

    private static final String REQUEST_PREFIX = ">> ";
    private static final String RESPONSE_PREFIX = "<< ";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        if (!log.isDebugEnabled()) {
            return chain.proceed(request);
        }

        StringBuilder reqLog = new StringBuilder();
        reqLog.append("\n")
              .append(REQUEST_PREFIX).append("=== HTTP REQUEST ===\n");

        reqLog.append(REQUEST_PREFIX)
              .append(request.method())
              .append(" ")
              .append(request.url())
              .append("\n");

        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            reqLog.append(REQUEST_PREFIX)
                  .append("[HEADER] ")
                  .append(headers.name(i))
                  .append(": ")
                  .append(headers.value(i))
                  .append("\n");
        }

        if (request.body() != null && isPlaintext(request.body().contentType())) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String body = buffer.readString(StandardCharsets.UTF_8);

            reqLog.append(REQUEST_PREFIX)
                  .append("Body: ")
                  .append(body)
                  .append("\n");
        }

        reqLog.append(REQUEST_PREFIX).append("====================");
        log.debug(reqLog.toString());

        Response response = chain.proceed(request);

        StringBuilder resLog = new StringBuilder();
        resLog.append("\n")
              .append(RESPONSE_PREFIX).append("=== HTTP RESPONSE ===\n");

        resLog.append(RESPONSE_PREFIX)
              .append("Status: ")
              .append(response.code())
              .append("\n");

        Headers resHeaders = response.headers();
        for (int i = 0; i < resHeaders.size(); i++) {
            resLog.append(RESPONSE_PREFIX)
                  .append("[HEADER] ")
                  .append(resHeaders.name(i))
                  .append(": ")
                  .append(resHeaders.value(i))
                  .append("\n");
        }

        ResponseBody responseBody = response.body();
        if (responseBody != null && isPlaintext(responseBody.contentType())) {
            MediaType contentType = responseBody.contentType();
            String bodyString = responseBody.string();

            resLog.append(RESPONSE_PREFIX)
                  .append("Body: ")
                  .append(bodyString)
                  .append("\n");

            response = response.newBuilder()
                               .body(ResponseBody.create(bodyString, contentType))
                               .build();
        }

        resLog.append(RESPONSE_PREFIX).append("====================");
        log.debug(resLog.toString());

        return response;
    }

    private boolean isPlaintext(MediaType contentType) {
        if (contentType == null) {
            return false;
        }

        String subtype = contentType.subtype().toLowerCase();
        return subtype.contains("json")
               || subtype.contains("xml")
               || subtype.contains("text")
               || subtype.contains("html")
               || subtype.contains("x-www-form-urlencoded");
    }
}
