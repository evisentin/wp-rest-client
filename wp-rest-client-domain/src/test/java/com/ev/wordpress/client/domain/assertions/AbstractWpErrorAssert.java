package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpError;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.Map;

public abstract class AbstractWpErrorAssert<SELF extends AbstractWpErrorAssert<SELF>>
        extends AbstractObjectAssert<SELF, WpError> {

    protected AbstractWpErrorAssert(final WpError actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF containsDataEntry(final String key, final Object expectedValue) {
        isNotNull();
        if (actual.getData() == null || !actual.getData().containsKey(key)) {
            failWithMessage("Expected error data to contain key <%s> but data was <%s>", key, actual.getData());
        }
        final Object actualValue = actual.getData().get(key);
        if (!java.util.Objects.equals(actualValue, expectedValue)) {
            failWithMessage("Expected error data entry <%s> to be <%s> but was <%s>", key, expectedValue, actualValue);
        }
        return myself;
    }

    public SELF hasCode(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getCode(), expected)) {
            failWithMessage("Expected error code to be <%s> but was <%s>", expected, actual.getCode());
        }
        return myself;
    }

    public SELF hasData(final Map<String, Object> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getData(), expected)) {
            failWithMessage("Expected error data to be <%s> but was <%s>", expected, actual.getData());
        }
        return myself;
    }

    public SELF hasMessage(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getMessage(), expected)) {
            failWithMessage("Expected error message to be <%s> but was <%s>", expected, actual.getMessage());
        }
        return myself;
    }
}
