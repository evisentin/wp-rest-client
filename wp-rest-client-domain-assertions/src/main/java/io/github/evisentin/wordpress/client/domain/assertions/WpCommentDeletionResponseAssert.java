package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.responses.WpCommentDeletionResponse;

public class WpCommentDeletionResponseAssert
        extends AbstractWpCommentDeletionResponseAssert<WpCommentDeletionResponseAssert> {

    public WpCommentDeletionResponseAssert(final WpCommentDeletionResponse actual) {
        super(actual, WpCommentDeletionResponseAssert.class);
    }

    public static WpCommentDeletionResponseAssert assertThat(final WpCommentDeletionResponse actual) {
        return new WpCommentDeletionResponseAssert(actual);
    }
}
