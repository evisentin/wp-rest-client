package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.WpMedia;

public class WpMediaAssert extends AbstractWpMediaAssert<WpMediaAssert> {

    public WpMediaAssert(final WpMedia actual) {
        super(actual, WpMediaAssert.class);
    }

    public static WpMediaAssert assertThat(final WpMedia actual) {
        return new WpMediaAssert(actual);
    }
}
