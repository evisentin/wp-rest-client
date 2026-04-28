package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpPost;

public class WpPostAssert extends AbstractWpPostAssert<WpPostAssert> {

    public WpPostAssert(final WpPost actual) {
        super(actual, WpPostAssert.class);
    }

    public static WpPostAssert assertThat(final WpPost actual) {
        return new WpPostAssert(actual);
    }
}
