package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpTag;

public class WpTagAssert extends AbstractWpTagAssert<WpTagAssert> {

    public WpTagAssert(final WpTag actual) {
        super(actual, WpTagAssert.class);
    }

    public static WpTagAssert assertThat(final WpTag actual) {
        return new WpTagAssert(actual);
    }
}
