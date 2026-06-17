package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;

public class WpPagedResponseAssert<T> extends AbstractWpPagedResponseAssert<WpPagedResponseAssert<T>, T> {

    public WpPagedResponseAssert(final WpPagedResponse<T> actual) {
        super(actual, WpPagedResponseAssert.class);
    }

    public static <T> WpPagedResponseAssert<T> assertThat(final WpPagedResponse<T> actual) {
        return new WpPagedResponseAssert<>(actual);
    }
}
