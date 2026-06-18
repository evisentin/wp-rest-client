package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.responses.WpCommentDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpCommentDeletionResponseAssert<S extends AbstractWpCommentDeletionResponseAssert<S>>
        extends AbstractObjectAssert<S, WpCommentDeletionResponse> {

    protected AbstractWpCommentDeletionResponseAssert(final WpCommentDeletionResponse actual,
                                                      final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasPreviousSatisfying(final Consumer<WpCommentAssert> requirements) {
        isNotNull();
        if (actual.getPrevious() == null) {
            failWithMessage("Expected previous comment to be present but it was null");
        }
        requirements.accept(new WpCommentAssert(actual.getPrevious()));
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
