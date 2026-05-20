package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.WpError;

public class WpErrorAssert extends AbstractWpErrorAssert<WpErrorAssert> {

    public WpErrorAssert(final WpError actual) {
        super(actual, WpErrorAssert.class);
    }

    public static WpErrorAssert assertThat(final WpError actual) {
        return new WpErrorAssert(actual);
    }
}
