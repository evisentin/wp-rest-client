package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpRenderedField;
import org.assertj.core.api.AbstractObjectAssert;

public abstract class AbstractWpRenderedFieldAssert<SELF extends AbstractWpRenderedFieldAssert<SELF>>
        extends AbstractObjectAssert<SELF, WpRenderedField> {

    protected AbstractWpRenderedFieldAssert(final WpRenderedField actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF hasBlockVersion(final Integer expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getBlockVersion(), expected)) {
            failWithMessage("Expected blockVersion to be <%s> but was <%s>", expected, actual.getBlockVersion());
        }
        return myself;
    }

    public SELF hasRaw(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getRaw(), expected)) {
            failWithMessage("Expected raw to be <%s> but was <%s>", expected, actual.getRaw());
        }
        return myself;
    }

    public SELF hasRendered(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getRendered(), expected)) {
            failWithMessage("Expected rendered to be <%s> but was <%s>", expected, actual.getRendered());
        }
        return myself;
    }

    public SELF isNotProtected() {
        isNotNull();
        if (!Boolean.FALSE.equals(actual.getIsProtected())) {
            failWithMessage("Expected field to be not protected but was <%s>", actual.getIsProtected());
        }
        return myself;
    }

    public SELF isProtected() {
        isNotNull();
        if (!Boolean.TRUE.equals(actual.getIsProtected())) {
            failWithMessage("Expected field to be protected but was <%s>", actual.getIsProtected());
        }
        return myself;
    }
}
