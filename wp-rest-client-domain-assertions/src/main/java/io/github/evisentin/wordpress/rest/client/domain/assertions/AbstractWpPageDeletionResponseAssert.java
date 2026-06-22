package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpPageDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpPageDeletionResponseAssert<S extends AbstractWpPageDeletionResponseAssert<S>>
        extends AbstractObjectAssert<S, WpPageDeletionResponse> {

    protected AbstractWpPageDeletionResponseAssert(final WpPageDeletionResponse actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasPreviousSatisfying(final Consumer<WpPageDeletionResponseSummaryAssert> requirements) {
        isNotNull();
        if (actual.getPrevious() == null) {
            failWithMessage("Expected previous summary to be present but it was null");
        }
        requirements.accept(new WpPageDeletionResponseSummaryAssert(actual.getPrevious()));
        return myself;
    }

    public S isDeleted() {
        isNotNull();
        if (!actual.isDeleted()) {
            failWithMessage("Expected response to be marked deleted but it was not");
        }
        return myself;
    }

    public S isNotDeleted() {
        isNotNull();
        if (actual.isDeleted()) {
            failWithMessage("Expected response not to be marked deleted but it was");
        }
        return myself;
    }
}
