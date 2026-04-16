package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpError;

public class WpErrorAssert extends AbstractWpErrorAssert<WpErrorAssert> {

    public WpErrorAssert(final WpError actual) {
        super(actual, WpErrorAssert.class);
    }

    public static WpErrorAssert assertThat(final WpError actual) {
        return new WpErrorAssert(actual);
    }
}
