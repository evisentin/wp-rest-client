package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPostDeletionResponse;

public class WpPostDeletionResponseAssert extends AbstractWpPostDeletionResponseAssert<WpPostDeletionResponseAssert> {

    public WpPostDeletionResponseAssert(final WpPostDeletionResponse actual) {
        super(actual, WpPostDeletionResponseAssert.class);
    }

    public static WpPostDeletionResponseAssert assertThat(final WpPostDeletionResponse actual) {
        return new WpPostDeletionResponseAssert(actual);
    }
}
