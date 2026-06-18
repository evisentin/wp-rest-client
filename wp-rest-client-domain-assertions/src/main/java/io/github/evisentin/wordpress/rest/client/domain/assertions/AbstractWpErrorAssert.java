package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpError;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.Map;

public abstract class AbstractWpErrorAssert<S extends AbstractWpErrorAssert<S>>
        extends AbstractObjectAssert<S, WpError> {

    protected AbstractWpErrorAssert(final WpError actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S containsDataEntry(final String key, final Object expectedValue) {
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

    public S hasCode(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getCode(), expected)) {
            failWithMessage("Expected error code to be <%s> but was <%s>", expected, actual.getCode());
        }
        return myself;
    }

    public S hasData(final Map<String, Object> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getData(), expected)) {
            failWithMessage("Expected error data to be <%s> but was <%s>", expected, actual.getData());
        }
        return myself;
    }

    public S hasMessage(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getMessage(), expected)) {
            failWithMessage("Expected error message to be <%s> but was <%s>", expected, actual.getMessage());
        }
        return myself;
    }
}
