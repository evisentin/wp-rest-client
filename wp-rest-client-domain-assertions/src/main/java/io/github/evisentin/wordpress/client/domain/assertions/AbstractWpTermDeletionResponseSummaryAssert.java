package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.enums.WpTaxonomy;
import io.github.evisentin.wordpress.client.domain.model.responses.WpTermDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpTermDeletionResponseSummaryAssert<S extends AbstractWpTermDeletionResponseSummaryAssert<S>>
        extends AbstractObjectAssert<S, WpTermDeletionResponse.Summary> {

    protected AbstractWpTermDeletionResponseSummaryAssert(final WpTermDeletionResponse.Summary actual,
                                                          final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasCount(final long expected) {
        isNotNull();
        if (actual.getCount() != expected) {
            failWithMessage("Expected previous.count to be <%s> but was <%s>", expected, actual.getCount());
        }
        return myself;
    }

    public S hasDescription(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDescription(), expected)) {
            failWithMessage("Expected previous.description to be <%s> but was <%s>", expected, actual.getDescription());
        }
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

    public S hasName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getName(), expected)) {
            failWithMessage("Expected previous.name to be <%s> but was <%s>", expected, actual.getName());
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

    public S hasTaxonomy(final WpTaxonomy expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTaxonomy(), expected)) {
            failWithMessage("Expected previous.taxonomy to be <%s> but was <%s>", expected, actual.getTaxonomy());
        }
        return myself;
    }
}
