package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;

public class WpPageDeletionResponseSummaryAssert
        extends AbstractWpPageDeletionResponseSummaryAssert<WpPageDeletionResponseSummaryAssert> {

    public WpPageDeletionResponseSummaryAssert(final WpPageDeletionResponse.Summary actual) {
        super(actual, WpPageDeletionResponseSummaryAssert.class);
    }

    public static WpPageDeletionResponseSummaryAssert assertThat(final WpPageDeletionResponse.Summary actual) {
        return new WpPageDeletionResponseSummaryAssert(actual);
    }
}
