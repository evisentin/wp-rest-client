package com.ev.wordpress.client.adapter.okhttp;

import com.ev.wordpress.client.domain.api.WpBaseRestClient;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import com.ev.wordpress.client.testsupport.AbstractBasicAuthenticationWpRestClientContractTest;

class OkHttpWpRestClientBasicAuthTest extends AbstractBasicAuthenticationWpRestClientContractTest {

    @Override
    protected WpBaseRestClient client() {
        return new OkHttpWpRestClient(mockServerUrl(), new WpBasicAuthenticationStrategy("user", "password"));
    }
}
