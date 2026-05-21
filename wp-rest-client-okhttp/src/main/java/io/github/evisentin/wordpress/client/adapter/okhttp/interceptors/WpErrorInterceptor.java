package io.github.evisentin.wordpress.client.adapter.okhttp.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.client.adapter.okhttp.http.HttpStatusCodes;
import io.github.evisentin.wordpress.client.domain.exception.WpBadRequestException;
import io.github.evisentin.wordpress.client.domain.exception.WpForbiddenException;
import io.github.evisentin.wordpress.client.domain.exception.WpNotFoundException;
import io.github.evisentin.wordpress.client.domain.exception.WpUnauthorizedException;
import io.github.evisentin.wordpress.client.domain.model.WpError;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * OkHttp {@link Interceptor} that translates non-successful HTTP responses from the WordPress REST API into
 * domain-specific exceptions.
 *
 * <p>This interceptor inspects the HTTP response returned by the server. If the
 * response is not successful (i.e., not in the 2xx range), it attempts to parse the response body into a
 * {@link WpError} object and maps the HTTP status code to a corresponding {@code WpException} subtype.
 *
 * <p>Supported mappings include:
 * <ul>
 *     <li>400 → {@link WpBadRequestException}</li>
 *     <li>401 → {@link WpUnauthorizedException}</li>
 *     <li>403 → {@link WpForbiddenException}</li>
 *     <li>404 → {@link WpNotFoundException}</li>
 * </ul>
 *
 * <p>If the response body cannot be parsed or the status code is not explicitly
 * handled, a generic {@link RuntimeException} is thrown.
 *
 * <p>This interceptor should typically be registered with the OkHttp client
 * used by the WordPress client.
 */
public final class WpErrorInterceptor implements Interceptor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Intercepts the HTTP request/response chain and converts error responses into domain-specific exceptions.
     *
     * <p>If the response is successful, it is returned unchanged. Otherwise,
     * the response body is read and parsed into a {@link WpError} object (if possible), and an appropriate exception is
     * thrown based on the HTTP status code.
     *
     * <p><strong>Note:</strong> The response body is consumed during processing,
     * so it is rebuilt before throwing the exception to preserve the original content.
     *
     * @param chain
     *         the OkHttp interceptor chain
     *
     * @return the original {@link Response} if successful
     *
     * @throws IOException
     *         if an I/O error occurs during request execution
     * @throws RuntimeException
     *         mapped exception corresponding to the HTTP error response
     */
    @Override
    @NotNull
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();
        final Response response = chain.proceed(request);

        if (response.isSuccessful()) {
            return response;
        }

        final ResponseBody originalBody = response.body();
        final MediaType contentType = originalBody.contentType();
        final String rawBody = originalBody.string();

        final WpError wpError = parseWpError(rawBody);

        final Response rebuiltResponse = response.newBuilder()
                                                 .body(ResponseBody.create(rawBody, contentType))
                                                 .build();

        throw mapException(rebuiltResponse, wpError, rawBody);
    }

    private static String buildBodySuffix(final String rawBody) {
        if (isBlank(rawBody))
            return "";

        return ", body=" + rawBody;
    }

    private static RuntimeException mapException(final Response response,
                                                 final WpError wpError,
                                                 final String rawBody) {
        return switch (response.code()) {
            case HttpStatusCodes.BAD_REQUEST -> new WpBadRequestException(wpError);
            case HttpStatusCodes.NOT_FOUND -> new WpNotFoundException(wpError);
            case HttpStatusCodes.UNAUTHORIZED -> new WpUnauthorizedException(wpError);
            case HttpStatusCodes.FORBIDDEN -> new WpForbiddenException(wpError);
            default -> new RuntimeException("Unexpected HTTP status " + response.code() + buildBodySuffix(rawBody));
        };
    }

    private static WpError parseWpError(final String rawBody) {
        if (isBlank(rawBody))
            return null;

        try {
            return MAPPER.readValue(rawBody, WpError.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
