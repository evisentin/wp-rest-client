package com.ev.wordpress.client.testsupport;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.Expectation;
import org.mockserver.serialization.ExpectationSerializer;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;

@Slf4j
@ExtendWith(MockServerExtension.class)
public abstract class AbstractMockServerTest implements WithAssertions {

    private final ExpectationSerializer expectationSerializer = new ExpectationSerializer(new MockServerLogger(log));
    protected MockServerClient mockServerClient;

    @BeforeEach
    @SneakyThrows
    void beforeEach(final MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
        this.mockServerClient.reset();
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
}
