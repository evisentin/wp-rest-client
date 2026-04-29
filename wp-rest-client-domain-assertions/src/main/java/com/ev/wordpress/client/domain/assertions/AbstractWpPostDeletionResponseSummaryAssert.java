package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.enums.WpPostStatus;
import com.ev.wordpress.client.domain.dto.responses.WpPostDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpPostDeletionResponseSummaryAssert<S extends AbstractWpPostDeletionResponseSummaryAssert<S>>
        extends AbstractObjectAssert<S, WpPostDeletionResponse.Summary> {

    protected AbstractWpPostDeletionResponseSummaryAssert(final WpPostDeletionResponse.Summary actual,
                                                          final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasContentSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getContent() == null) {
            failWithMessage("Expected previous.content to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getContent()));
        return myself;
    }

    public S hasExcerptSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getExcerpt() == null) {
            failWithMessage("Expected previous.excerpt to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getExcerpt()));
        return myself;
    }

    public S hasId(final long expected) {
        isNotNull();
        if (actual.getId() != expected) {
            failWithMessage("Expected previous.id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public S hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected previous.link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public S hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected previous.slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public S hasStatus(final WpPostStatus expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getStatus(), expected)) {
            failWithMessage("Expected previous.status to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }

    public S hasTitleSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getTitle() == null) {
            failWithMessage("Expected previous.title to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getTitle()));
        return myself;
    }
}
