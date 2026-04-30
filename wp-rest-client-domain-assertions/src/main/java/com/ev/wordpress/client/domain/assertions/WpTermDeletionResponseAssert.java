package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.model.responses.WpTermDeletionResponse;

public class WpTermDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpTermDeletionResponseAssert, WpTermDeletionResponse> {

    public WpTermDeletionResponseAssert(final WpTermDeletionResponse actual) {
        super(actual, WpTermDeletionResponseAssert.class);
    }

    public static WpTermDeletionResponseAssert assertThat(final WpTermDeletionResponse actual) {
        return new WpTermDeletionResponseAssert(actual);
    }
}
