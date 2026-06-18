package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpTermDeletionResponse;

public class WpTermDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpTermDeletionResponseAssert, WpTermDeletionResponse> {

    public WpTermDeletionResponseAssert(final WpTermDeletionResponse actual) {
        super(actual, WpTermDeletionResponseAssert.class);
    }

    public static WpTermDeletionResponseAssert assertThat(final WpTermDeletionResponse actual) {
        return new WpTermDeletionResponseAssert(actual);
    }
}
