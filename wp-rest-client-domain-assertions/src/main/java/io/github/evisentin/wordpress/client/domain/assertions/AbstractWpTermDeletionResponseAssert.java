package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.responses.WpTermDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpTermDeletionResponseAssert<S extends AbstractWpTermDeletionResponseAssert<S, A>, A extends WpTermDeletionResponse>
        extends AbstractObjectAssert<S, A> {

    protected AbstractWpTermDeletionResponseAssert(final A actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasPreviousSatisfying(final Consumer<WpTermDeletionResponseSummaryAssert> requirements) {
        isNotNull();
        if (actual.getPrevious() == null) {
            failWithMessage("Expected previous summary to be present but it was null");
        }
        requirements.accept(new WpTermDeletionResponseSummaryAssert(actual.getPrevious()));
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
