package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpTag;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpTaxonomy;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpTagAssert<S extends AbstractWpTagAssert<S>>
        extends AbstractObjectAssert<S, WpTag> {

    protected AbstractWpTagAssert(final WpTag actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasCount(final long expected) {
        isNotNull();
        if (actual.getCount() != expected) {
            failWithMessage("Expected tag count to be <%s> but was <%s>", expected, actual.getCount());
        }
        return myself;
    }

    public S hasDescription(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDescription(), expected)) {
            failWithMessage("Expected tag description to be <%s> but was <%s>", expected, actual.getDescription());
        }
        return myself;
    }

    public S hasId(final long expected) {
        isNotNull();
        if (actual.getId() != expected) {
            failWithMessage("Expected tag id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public S hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected tag link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public S hasName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getName(), expected)) {
            failWithMessage("Expected tag name to be <%s> but was <%s>", expected, actual.getName());
        }
        return myself;
    }

    public S hasNonZeroId() {
        isNotNull();

        if (actual.getId() == 0L) {
            failWithMessage("Expected tag id to be non-zero but was <0>");
        }

        return myself;
    }

    public S hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected tag slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public S hasTaxonomy(final WpTaxonomy expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTaxonomy(), expected)) {
            failWithMessage("Expected tag taxonomy to be <%s> but was <%s>", expected, actual.getTaxonomy());
        }
        return myself;
    }
}
