package io.github.evisentin.wordpress.rest.client.contract.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.Expectation;
import org.mockserver.serialization.ExpectationSerializer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Slf4j
public abstract class AbstractMockServerTest implements WithAssertions {

    private final ExpectationSerializer expectationSerializer = new ExpectationSerializer(new MockServerLogger(log));
    private final ClientAndServer mockServerClient = ClientAndServer.startClientAndServer(0);

    @BeforeEach
    void beforeEach() {
        mockServerClient.reset();
        setUpMockApiUrlDiscovery(this.mockServerClient);
    }

    @SneakyThrows
    protected void givenExpectationFromFile(final String resourcePath) {

        String json = resourceToString("/mockserver/expectations/" + resourcePath, UTF_8);

        json = Strings.CS.replace(json, "${MOCKSERVER_URL}", mockServerUrl());

        final Expectation[] expectations = expectationSerializer.deserializeArray(json, true);

        mockServerClient.upsert(expectations);
    }

    protected String mockServerUrl() {
        return String.format("http://%s:%d", mockServerClient.remoteAddress().getHostName(), mockServerClient.remoteAddress().getPort());
    }

    private void setUpMockApiUrlDiscovery(final MockServerClient mockServerClient) {

        mockServerClient
                .when(request().withMethod("HEAD")
                               .withPath("/")
                )
                .respond(
                        response().withStatusCode(200)
                                  .withHeader("Link", "<" + mockServerUrl() + "/wp-json/>; rel=\"https://api.w.org/\"")
                                  .withHeader("Content-Type", "text/html; charset=UTF-8")
                );
    }
}
