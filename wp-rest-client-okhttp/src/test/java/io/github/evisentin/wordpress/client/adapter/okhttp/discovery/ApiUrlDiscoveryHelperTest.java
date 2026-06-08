package io.github.evisentin.wordpress.client.adapter.okhttp.discovery;

import io.github.evisentin.wordpress.client.domain.exception.ApiUrlNotFoundException;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class ApiUrlDiscoveryHelperTest implements WithAssertions {

    private final ClientAndServer mockServer = ClientAndServer.startClientAndServer(0);

    @BeforeEach
    void beforeEach() {
        mockServer.reset();
    }

    @Test
    @SneakyThrows
    void resolveApiUrlFailsOnNullParameters() {

        assertThatThrownBy(() -> ApiUrlDiscoveryHelper.resolveApiUrl(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("httpClient is marked non-null but is null");

        assertThatThrownBy(() -> ApiUrlDiscoveryHelper.resolveApiUrl(new OkHttpClient(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("baseUrl is marked non-null but is null");
    }

    @Test
    @SneakyThrows
    void resolveApiUrlReturnsWordPressApiUrlFromLinkHeader() {

        final OkHttpClient httpClient = new OkHttpClient();

        final String baseUrl = "http://localhost:" + mockServer.getLocalPort();
        final String apiUrl = baseUrl + "/wp-json/";

        mockServer
                .when(request()
                        .withMethod("HEAD")
                        .withPath("/"))
                .respond(response()
                        .withStatusCode(200)
                        .withHeader("Link", "<" + apiUrl + ">; rel=\"https://api.w.org/\"")
                        .withHeader("Content-Type", "text/html; charset=UTF-8"));

        assertThat(ApiUrlDiscoveryHelper.resolveApiUrl(httpClient, baseUrl))
                .isEqualTo(baseUrl + "/wp-json");
    }

    @Test
    @SneakyThrows
    void resolveApiUrlThrowsApiUrlNotFoundExceptionWhenLinkHeaderIsMissing() {

        final OkHttpClient httpClient = new OkHttpClient();

        final String baseUrl = "http://localhost:" + mockServer.getLocalPort();

        mockServer
                .when(request()
                        .withMethod("HEAD")
                        .withPath("/"))
                .respond(response()
                        .withStatusCode(200));

        assertThatThrownBy(() -> ApiUrlDiscoveryHelper.resolveApiUrl(httpClient, baseUrl))
                .isInstanceOf(ApiUrlNotFoundException.class);
    }
}
