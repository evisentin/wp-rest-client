package com.ev.wordpress.client.testsupport;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;

public abstract class AbstractJwtAuthenticationWpRestClientContractTest extends AbstractMockServerTest {
    protected abstract WpBaseRestClient client();
}
