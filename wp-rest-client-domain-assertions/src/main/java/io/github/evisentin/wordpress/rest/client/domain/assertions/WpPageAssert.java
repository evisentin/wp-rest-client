package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPage;

public class WpPageAssert extends AbstractWpPageAssert<WpPageAssert> {

    public WpPageAssert(final WpPage actual) {
        super(actual, WpPageAssert.class);
    }

    public static WpPageAssert assertThat(final WpPage actual) {
        return new WpPageAssert(actual);
    }
}
