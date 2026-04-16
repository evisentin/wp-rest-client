package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.responses.WpPostDeletionResponse;

public class WpPostDeletionResponseAssert extends AbstractWpPostDeletionResponseAssert<WpPostDeletionResponseAssert> {

    public WpPostDeletionResponseAssert(final WpPostDeletionResponse actual) {
        super(actual, WpPostDeletionResponseAssert.class);
    }

    public static WpPostDeletionResponseAssert assertThat(final WpPostDeletionResponse actual) {
        return new WpPostDeletionResponseAssert(actual);
    }
}
