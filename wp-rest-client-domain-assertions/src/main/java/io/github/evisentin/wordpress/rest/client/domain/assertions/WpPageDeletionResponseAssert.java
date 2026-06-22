package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;

public class WpPageDeletionResponseAssert extends AbstractWpPageDeletionResponseAssert<WpPageDeletionResponseAssert> {

    public WpPageDeletionResponseAssert(final WpPageDeletionResponse actual) {
        super(actual, WpPageDeletionResponseAssert.class);
    }

    public static WpPageDeletionResponseAssert assertThat(final WpPageDeletionResponse actual) {
        return new WpPageDeletionResponseAssert(actual);
    }
}
