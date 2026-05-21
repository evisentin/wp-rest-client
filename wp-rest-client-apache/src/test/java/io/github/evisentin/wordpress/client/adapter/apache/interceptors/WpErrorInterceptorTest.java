package io.github.evisentin.wordpress.client.adapter.apache.interceptors;

import io.github.evisentin.wordpress.client.domain.exception.WpBadRequestException;
import io.github.evisentin.wordpress.client.domain.exception.WpForbiddenException;
import io.github.evisentin.wordpress.client.domain.exception.WpNotFoundException;
import io.github.evisentin.wordpress.client.domain.exception.WpUnauthorizedException;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WpErrorInterceptorTest {

    private final WpErrorInterceptor interceptor = new WpErrorInterceptor();

    @Test
    @DisplayName("should do nothing for successful response")
    void shouldDoNothingForSuccessfulResponse() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(new StringEntity("{\"ok\":true}"));

        org.assertj.core.api.Assertions.assertThatCode(() ->
                interceptor.process(response, null, null)
        ).doesNotThrowAnyException();

        assertThat(response.getEntity()).isNotNull();
    }

    @Test
    @DisplayName("should rebuild empty entity when classic response has no body")
    void shouldRebuildEmptyEntityWhenClassicResponseHasNoBody() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(400);

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpBadRequestException.class);

        assertThat(response.getEntity()).isNotNull();
        assertThat(entityBody(response)).isEmpty();
    }

    @Test
    @DisplayName("should throw bad request exception for status 400")
    void shouldThrowBadRequestExceptionForStatus400() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(400);
        response.setEntity(new StringEntity("{\"code\":\"bad_request\",\"message\":\"Invalid request\"}"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpBadRequestException.class);

        assertThat(entityBody(response)).isEqualTo("{\"code\":\"bad_request\",\"message\":\"Invalid request\"}");
    }

    @Test
    @DisplayName("should throw forbidden exception for status 403")
    void shouldThrowForbiddenExceptionForStatus403() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(403);
        response.setEntity(new StringEntity("{\"code\":\"forbidden\",\"message\":\"Forbidden\"}"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpForbiddenException.class);

        assertThat(entityBody(response)).isEqualTo("{\"code\":\"forbidden\",\"message\":\"Forbidden\"}");
    }

    @Test
    @DisplayName("should throw mapped exception when body is invalid json")
    void shouldThrowMappedExceptionWhenBodyIsInvalidJson() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(400);
        response.setEntity(new StringEntity("not-json"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpBadRequestException.class);

        org.assertj.core.api.Assertions.assertThat(entityBody(response)).isEqualTo("not-json");
    }

    @Test
    @DisplayName("should throw mapped exception when error body is missing")
    void shouldThrowMappedExceptionWhenErrorBodyIsMissing() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(400);

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpBadRequestException.class);
    }

    @Test
    @DisplayName("should throw not found exception for status 404")
    void shouldThrowNotFoundExceptionForStatus404() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(404);
        response.setEntity(new StringEntity("{\"code\":\"not_found\",\"message\":\"Not found\"}"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpNotFoundException.class);

        assertThat(entityBody(response)).isEqualTo("{\"code\":\"not_found\",\"message\":\"Not found\"}");
    }

    @Test
    @DisplayName("should throw runtime exception with body for unexpected status")
    void shouldThrowRuntimeExceptionWithBodyForUnexpectedStatus() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(500);
        response.setEntity(new StringEntity("boom"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected HTTP status 500, body=boom");

        assertThat(entityBody(response)).isEqualTo("boom");
    }

    @Test
    @DisplayName("should throw runtime exception without body suffix for blank body")
    void shouldThrowRuntimeExceptionWithoutBodySuffixForBlankBody() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(500);
        response.setEntity(new StringEntity("   "));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected HTTP status 500");

        assertThat(entityBody(response)).isEqualTo("   ");
    }

    @Test
    @DisplayName("should throw runtime exception without body when response is not classic")
    void shouldThrowRuntimeExceptionWithoutBodyWhenResponseIsNotClassic() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.getCode()).thenReturn(500);

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unexpected HTTP status 500");
    }

    @Test
    @DisplayName("should throw unauthorized exception for status 401")
    void shouldThrowUnauthorizedExceptionForStatus401() {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(401);
        response.setEntity(new StringEntity("{\"code\":\"unauthorized\",\"message\":\"Unauthorized\"}"));

        assertThatThrownBy(() -> interceptor.process(response, null, null))
                .isInstanceOf(WpUnauthorizedException.class);

        assertThat(entityBody(response)).isEqualTo("{\"code\":\"unauthorized\",\"message\":\"Unauthorized\"}");
    }

    private static String entityBody(BasicClassicHttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException | HttpException e) {
            throw new RuntimeException(e);
        }
    }
}
