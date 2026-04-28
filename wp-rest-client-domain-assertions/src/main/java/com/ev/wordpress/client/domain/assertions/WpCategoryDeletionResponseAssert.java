package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.responses.WpCategoryDeletionResponse;

public class WpCategoryDeletionResponseAssert
        extends AbstractWpTermDeletionResponseAssert<WpCategoryDeletionResponseAssert, WpCategoryDeletionResponse> {

    public WpCategoryDeletionResponseAssert(final WpCategoryDeletionResponse actual) {
        super(actual, WpCategoryDeletionResponseAssert.class);
    }

    public static WpCategoryDeletionResponseAssert assertThat(final WpCategoryDeletionResponse actual) {
        return new WpCategoryDeletionResponseAssert(actual);
    }
}
