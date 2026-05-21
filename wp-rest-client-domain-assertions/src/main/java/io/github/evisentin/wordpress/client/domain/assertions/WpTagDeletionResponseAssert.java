package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.responses.WpTagDeletionResponse;

public class WpTagDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpTagDeletionResponseAssert, WpTagDeletionResponse> {

    public WpTagDeletionResponseAssert(final WpTagDeletionResponse actual) {
        super(actual, WpTagDeletionResponseAssert.class);
    }

    public static WpTagDeletionResponseAssert assertThat(final WpTagDeletionResponse actual) {
        return new WpTagDeletionResponseAssert(actual);
    }
}
