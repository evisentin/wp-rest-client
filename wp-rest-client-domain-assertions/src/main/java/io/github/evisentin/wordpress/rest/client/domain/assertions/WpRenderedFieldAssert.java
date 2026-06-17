package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpRenderedField;

public class WpRenderedFieldAssert extends AbstractWpRenderedFieldAssert<WpRenderedFieldAssert> {

    public WpRenderedFieldAssert(final WpRenderedField actual) {
        super(actual, WpRenderedFieldAssert.class);
    }

    public static WpRenderedFieldAssert assertThat(final WpRenderedField actual) {
        return new WpRenderedFieldAssert(actual);
    }
}
