package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.client.domain.exception.WpForbiddenException;
import com.ev.wordpress.client.domain.exception.WpNotFoundException;
import com.ev.wordpress.client.domain.exception.WpUnauthorizedException;
import okhttp3.*;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.stream.Stream;

import static com.ev.wordpress.client.adapter.okhttp.constants.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WpErrorInterceptorTest implements WithAssertions {

    private final WpErrorInterceptor interceptor = new WpErrorInterceptor();

    @Mock
    private Interceptor.Chain chain;

    private InOrder inOrder;

    @BeforeEach
    void beforeEach() {
        inOrder = Mockito.inOrder(chain);
    }

    @Test
    void shouldIgnoreInvalidJsonAndStillMapStatusCode() throws IOException {
        // GIVEN
        mockChainReturning(400, "not-json");

        // WHEN / THEN
        assertThatThrownBy(() -> interceptor.intercept(chain))
                .isInstanceOf(WpBadRequestException.class);
    }

    @Test
    void shouldReturnResponseWhenSuccessful() throws IOException {
        // GIVEN

        Request request = new Request.Builder()
                .url("https://example.com/wp-json/test")
                .build();

        Response response = response(request, 200, "{\"ok\":true}");

        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);

        // WHEN
        Response actual = interceptor.intercept(chain);

        // THEN
        assertThat(actual).isSameAs(response);

        inOrder.verify(chain).request();
        inOrder.verify(chain).proceed(request);
        inOrder.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @MethodSource("mappedStatusCodes")
    void shouldThrowMappedExceptionForKnownStatusCodes(
            int statusCode,
            String body,
            Class<? extends RuntimeException> expectedExceptionType
    ) throws IOException {
        // GIVEN
        mockChainReturning(statusCode, body);

        // WHEN / THEN
        assertThatThrownBy(() -> interceptor.intercept(chain))
                .isInstanceOf(expectedExceptionType);
    }

    @Test
    void shouldThrowRuntimeExceptionWithBodyForUnexpectedStatus() throws IOException {
        // GIVEN
        mockChainReturning(500, "{\"error\":\"boom\"}");

        // WHEN / THEN
        assertThatThrownBy(() -> interceptor.intercept(chain))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected HTTP status 500, body={\"error\":\"boom\"}");
    }

    @Test
    void shouldThrowRuntimeExceptionWithoutBodySuffixWhenBodyIsBlank() throws IOException {
        // GIVEN
        mockChainReturning(500, "");

        // WHEN / THEN
        assertThatThrownBy(() -> interceptor.intercept(chain))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected HTTP status 500");
    }

    private void mockChainReturning(int statusCode, String body) throws IOException {

        Request request = new Request.Builder()
                .url("https://example.com/wp-json/test")
                .build();

        Response response = response(request, statusCode, body);

        when(chain.request()).thenReturn(request);
        when(chain.proceed(request)).thenReturn(response);
    }

    private static Stream<Arguments> mappedStatusCodes() {
        return Stream.of(
                Arguments.of(400, "{\"code\":\"bad_request\",\"message\":\"invalid\"}", WpBadRequestException.class),
                Arguments.of(404, "{\"code\":\"not_found\",\"message\":\"missing\"}", WpNotFoundException.class),
                Arguments.of(401, "{\"code\":\"unauthorized\",\"message\":\"auth required\"}", WpUnauthorizedException.class),
                Arguments.of(403, "{\"code\":\"forbidden\",\"message\":\"denied\"}", WpForbiddenException.class)
        );
    }

    private static Response response(Request request, int statusCode, String body) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .message("test")
                .body(ResponseBody.create(body, MEDIA_TYPE_APPLICATION_JSON))
                .build();
    }
}
