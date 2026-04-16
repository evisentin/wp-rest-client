package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpTag;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpTagAssert<SELF extends AbstractWpTagAssert<SELF>>
        extends AbstractObjectAssert<SELF, WpTag> {

    protected AbstractWpTagAssert(final WpTag actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF hasCount(final long expected) {
        isNotNull();
        if (actual.getCount() != expected) {
            failWithMessage("Expected tag count to be <%s> but was <%s>", expected, actual.getCount());
        }
        return myself;
    }

    public SELF hasDescription(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDescription(), expected)) {
            failWithMessage("Expected tag description to be <%s> but was <%s>", expected, actual.getDescription());
        }
        return myself;
    }

    public SELF hasId(final long expected) {
        isNotNull();
        if (actual.getId() != expected) {
            failWithMessage("Expected tag id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public SELF hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected tag link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public SELF hasName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getName(), expected)) {
            failWithMessage("Expected tag name to be <%s> but was <%s>", expected, actual.getName());
        }
        return myself;
    }

    public SELF hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected tag slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public SELF hasTaxonomy(final WpTaxonomy expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTaxonomy(), expected)) {
            failWithMessage("Expected tag taxonomy to be <%s> but was <%s>", expected, actual.getTaxonomy());
        }
        return myself;
    }
}
