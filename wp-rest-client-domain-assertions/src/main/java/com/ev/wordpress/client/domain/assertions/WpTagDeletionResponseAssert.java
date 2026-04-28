package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.responses.WpTagDeletionResponse;

public class WpTagDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpTagDeletionResponseAssert, WpTagDeletionResponse> {

    public WpTagDeletionResponseAssert(final WpTagDeletionResponse actual) {
        super(actual, WpTagDeletionResponseAssert.class);
    }

    public static WpTagDeletionResponseAssert assertThat(final WpTagDeletionResponse actual) {
        return new WpTagDeletionResponseAssert(actual);
    }
}
