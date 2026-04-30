package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.model.*;
import com.ev.wordpress.client.domain.model.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.client.domain.model.responses.WpPostDeletionResponse;
import com.ev.wordpress.client.domain.model.responses.WpTagDeletionResponse;
import com.ev.wordpress.client.domain.model.responses.WpTermDeletionResponse;

public final class WordPressAssertions {

    private WordPressAssertions() {
    }

    public static WpCategoryAssert assertThat(final WpCategory actual) {
        return new WpCategoryAssert(actual);
    }

    public static WpErrorAssert assertThat(final WpError actual) {
        return new WpErrorAssert(actual);
    }

    public static WpRenderedFieldAssert assertThat(final WpRenderedField actual) {
        return new WpRenderedFieldAssert(actual);
    }

    public static WpTagAssert assertThat(final WpTag actual) {
        return new WpTagAssert(actual);
    }

    public static WpPostAssert assertThat(final WpPost actual) {
        return new WpPostAssert(actual);
    }

    public static <T> WpPagedResponseAssert<T> assertThat(final WpPagedResponse<T> actual) {
        return new WpPagedResponseAssert<>(actual);
    }

    public static WpTermDeletionResponseAssert assertThat(final WpTermDeletionResponse actual) {
        return new WpTermDeletionResponseAssert(actual);
    }

    public static WpCategoryDeletionResponseAssert assertThat(final WpCategoryDeletionResponse actual) {
        return new WpCategoryDeletionResponseAssert(actual);
    }

    public static WpTagDeletionResponseAssert assertThat(final WpTagDeletionResponse actual) {
        return new WpTagDeletionResponseAssert(actual);
    }

    public static WpPostDeletionResponseAssert assertThat(final WpPostDeletionResponse actual) {
        return new WpPostDeletionResponseAssert(actual);
    }
}
