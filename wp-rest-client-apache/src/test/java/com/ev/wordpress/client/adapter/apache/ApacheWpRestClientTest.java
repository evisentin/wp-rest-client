package com.ev.wordpress.client.adapter.apache;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.testsupport.AbstractBasicAuthenticationWpRestClientContractTest;

class ApacheWpRestClientTest extends AbstractBasicAuthenticationWpRestClientContractTest {

    @Override
    protected WpBaseRestClient client() {
        final WpBasicAuthenticationStrategy authenticationStrategy = new WpBasicAuthenticationStrategy("user", "password");

        return new ApacheWpRestClient(mockServerUrl(), authenticationStrategy);
    }
}
