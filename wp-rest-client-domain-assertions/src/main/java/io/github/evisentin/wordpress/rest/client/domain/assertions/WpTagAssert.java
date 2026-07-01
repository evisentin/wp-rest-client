package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpTag;

public class WpTagAssert extends AbstractWpTagAssert<WpTagAssert> {

    public WpTagAssert(final WpTag actual) {
        super(actual, WpTagAssert.class);
    }

    public static WpTagAssert assertThat(final WpTag actual) {
        return new WpTagAssert(actual);
    }
}
