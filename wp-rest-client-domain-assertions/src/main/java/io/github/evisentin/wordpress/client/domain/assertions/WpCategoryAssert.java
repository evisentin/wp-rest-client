package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.WpCategory;

public class WpCategoryAssert extends AbstractWpCategoryAssert<WpCategoryAssert> {

    public WpCategoryAssert(final WpCategory actual) {
        super(actual, WpCategoryAssert.class);
    }

    public static WpCategoryAssert assertThat(final WpCategory actual) {
        return new WpCategoryAssert(actual);
    }
}
