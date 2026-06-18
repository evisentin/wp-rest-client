package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpCategoryDeletionResponse;

public class WpCategoryDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpCategoryDeletionResponseAssert, WpCategoryDeletionResponse> {

    public WpCategoryDeletionResponseAssert(final WpCategoryDeletionResponse actual) {
        super(actual, WpCategoryDeletionResponseAssert.class);
    }

    public static WpCategoryDeletionResponseAssert assertThat(final WpCategoryDeletionResponse actual) {
        return new WpCategoryDeletionResponseAssert(actual);
    }
}
