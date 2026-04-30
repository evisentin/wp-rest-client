package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.model.responses.WpTermDeletionResponse;

public class WpTermDeletionResponseSummaryAssert
        extends AbstractWpTermDeletionResponseSummaryAssert<WpTermDeletionResponseSummaryAssert> {

    public WpTermDeletionResponseSummaryAssert(final WpTermDeletionResponse.Summary actual) {
        super(actual, WpTermDeletionResponseSummaryAssert.class);
    }

    public static WpTermDeletionResponseSummaryAssert assertThat(final WpTermDeletionResponse.Summary actual) {
        return new WpTermDeletionResponseSummaryAssert(actual);
    }
}
