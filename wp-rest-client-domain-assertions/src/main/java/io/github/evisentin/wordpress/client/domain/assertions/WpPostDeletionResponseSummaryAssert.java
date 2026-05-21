package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.responses.WpPostDeletionResponse;

public class WpPostDeletionResponseSummaryAssert
        extends AbstractWpPostDeletionResponseSummaryAssert<WpPostDeletionResponseSummaryAssert> {

    public WpPostDeletionResponseSummaryAssert(final WpPostDeletionResponse.Summary actual) {
        super(actual, WpPostDeletionResponseSummaryAssert.class);
    }

    public static WpPostDeletionResponseSummaryAssert assertThat(final WpPostDeletionResponse.Summary actual) {
        return new WpPostDeletionResponseSummaryAssert(actual);
    }
}
