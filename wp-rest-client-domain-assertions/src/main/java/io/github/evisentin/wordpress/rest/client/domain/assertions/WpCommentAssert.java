package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpComment;

public class WpCommentAssert extends AbstractWpCommentAssert<WpCommentAssert> {

    public WpCommentAssert(final WpComment actual) {
        super(actual, WpCommentAssert.class);
    }

    public static WpCommentAssert assertThat(final WpComment actual) {
        return new WpCommentAssert(actual);
    }
}
