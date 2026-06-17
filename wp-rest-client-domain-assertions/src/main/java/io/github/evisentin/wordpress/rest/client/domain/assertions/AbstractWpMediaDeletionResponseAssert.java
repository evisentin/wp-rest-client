package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpMediaDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpMediaDeletionResponseAssert<S extends AbstractWpMediaDeletionResponseAssert<S>>
        extends AbstractObjectAssert<S, WpMediaDeletionResponse> {

    protected AbstractWpMediaDeletionResponseAssert(final WpMediaDeletionResponse actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasPreviousSatisfying(final Consumer<WpMediaAssert> requirements) {
        isNotNull();
        if (actual.getPrevious() == null) {
            failWithMessage("Expected previous summary to be present but it was null");
        }
        requirements.accept(new WpMediaAssert(actual.getPrevious()));
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
