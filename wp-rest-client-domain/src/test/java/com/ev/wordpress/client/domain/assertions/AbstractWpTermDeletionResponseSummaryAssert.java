package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.responses.WpTermDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpTermDeletionResponseSummaryAssert<SELF extends AbstractWpTermDeletionResponseSummaryAssert<SELF>>
        extends AbstractObjectAssert<SELF, WpTermDeletionResponse.Summary> {

    protected AbstractWpTermDeletionResponseSummaryAssert(final WpTermDeletionResponse.Summary actual,
                                                          final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF hasCount(final long expected) {
        isNotNull();
        if (actual.getCount() != expected) {
            failWithMessage("Expected previous.count to be <%s> but was <%s>", expected, actual.getCount());
        }
        return myself;
    }

    public SELF hasDescription(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDescription(), expected)) {
            failWithMessage("Expected previous.description to be <%s> but was <%s>", expected, actual.getDescription());
        }
        return myself;
    }

    public SELF hasId(final long expected) {
        isNotNull();
        if (actual.getId() != expected) {
            failWithMessage("Expected previous.id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public SELF hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected previous.link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public SELF hasName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getName(), expected)) {
            failWithMessage("Expected previous.name to be <%s> but was <%s>", expected, actual.getName());
        }
        return myself;
    }

    public SELF hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected previous.slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public SELF hasTaxonomy(final WpTaxonomy expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTaxonomy(), expected)) {
            failWithMessage("Expected previous.taxonomy to be <%s> but was <%s>", expected, actual.getTaxonomy());
        }
        return myself;
    }
}
