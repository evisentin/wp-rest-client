package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.WpPost;

public class WpPostAssert extends AbstractWpPostAssert<WpPostAssert> {

    public WpPostAssert(final WpPost actual) {
        super(actual, WpPostAssert.class);
    }

    public static WpPostAssert assertThat(final WpPost actual) {
        return new WpPostAssert(actual);
    }
}
