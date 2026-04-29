package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpCategoryAssert<S extends AbstractWpCategoryAssert<S>>
        extends AbstractObjectAssert<S, WpCategory> {

    protected AbstractWpCategoryAssert(final WpCategory actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasCount(final long expected) {
        isNotNull();
        if (actual.getCount() != expected) {
            failWithMessage("Expected category count to be <%s> but was <%s>", expected, actual.getCount());
        }
        return myself;
    }

    public S hasDescription(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDescription(), expected)) {
            failWithMessage("Expected category description to be <%s> but was <%s>", expected, actual.getDescription());
        }
        return myself;
    }

    public S hasId(final long expected) {
        isNotNull();
        if (actual.getId() != expected) {
            failWithMessage("Expected category id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public S hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected category link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public S hasName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getName(), expected)) {
            failWithMessage("Expected category name to be <%s> but was <%s>", expected, actual.getName());
        }
        return myself;
    }

    public S hasNoParent() {
        isNotNull();
        if (actual.getParentId() != 0) {
            failWithMessage("Expected category to have no parent but parentId was <%s>", actual.getParentId());
        }
        return myself;
    }

    public S hasNonZeroId() {
        isNotNull();

        if (actual.getId() == 0L) {
            failWithMessage("Expected category id to be non-zero but was <0>");
        }

        return myself;
    }

    public S hasParentId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getParentId(), expected)) {
            failWithMessage("Expected category parentId to be <%s> but was <%s>", expected, actual.getParentId());
        }
        return myself;
    }

    public S hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected category slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public S hasTaxonomy(final WpTaxonomy expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTaxonomy(), expected)) {
            failWithMessage("Expected category taxonomy to be <%s> but was <%s>", expected, actual.getTaxonomy());
        }
        return myself;
    }
}
