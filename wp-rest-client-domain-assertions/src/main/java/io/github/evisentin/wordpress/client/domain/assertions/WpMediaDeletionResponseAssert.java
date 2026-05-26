package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.responses.WpMediaDeletionResponse;

public class WpMediaDeletionResponseAssert extends AbstractWpMediaDeletionResponseAssert<WpMediaDeletionResponseAssert> {

    public WpMediaDeletionResponseAssert(final WpMediaDeletionResponse actual) {
        super(actual, WpMediaDeletionResponseAssert.class);
    }

    public static WpMediaDeletionResponseAssert assertThat(final WpMediaDeletionResponse actual) {
        return new WpMediaDeletionResponseAssert(actual);
    }
}
