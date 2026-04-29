package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpRenderedField;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpRenderedFieldAssert<S extends AbstractWpRenderedFieldAssert<S>>
        extends AbstractObjectAssert<S, WpRenderedField> {

    protected AbstractWpRenderedFieldAssert(final WpRenderedField actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasBlockVersion(final Integer expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getBlockVersion(), expected)) {
            failWithMessage("Expected blockVersion to be <%s> but was <%s>", expected, actual.getBlockVersion());
        }
        return myself;
    }

    public S hasRaw(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getRaw(), expected)) {
            failWithMessage("Expected raw to be <%s> but was <%s>", expected, actual.getRaw());
        }
        return myself;
    }

    public S hasRendered(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getRendered(), expected)) {
            failWithMessage("Expected rendered to be <%s> but was <%s>", expected, actual.getRendered());
        }
        return myself;
    }

    public S isNotProtected() {
        isNotNull();
        if (!Boolean.FALSE.equals(actual.getIsProtected())) {
            failWithMessage("Expected field to be not protected but was <%s>", actual.getIsProtected());
        }
        return myself;
    }

    public S isProtected() {
        isNotNull();
        if (!Boolean.TRUE.equals(actual.getIsProtected())) {
            failWithMessage("Expected field to be protected but was <%s>", actual.getIsProtected());
        }
        return myself;
    }
}
