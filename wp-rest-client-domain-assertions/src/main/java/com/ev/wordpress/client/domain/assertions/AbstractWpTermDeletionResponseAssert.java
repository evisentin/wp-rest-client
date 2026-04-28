package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.responses.WpTermDeletionResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Consumer;

public abstract class AbstractWpTermDeletionResponseAssert<SELF extends AbstractWpTermDeletionResponseAssert<SELF, ACTUAL>, ACTUAL extends WpTermDeletionResponse>
        extends AbstractObjectAssert<SELF, ACTUAL> {

    protected AbstractWpTermDeletionResponseAssert(final ACTUAL actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF hasPreviousSatisfying(final Consumer<WpTermDeletionResponseSummaryAssert> requirements) {
        isNotNull();
        if (actual.getPrevious() == null) {
            failWithMessage("Expected previous summary to be present but it was null");
        }
        requirements.accept(new WpTermDeletionResponseSummaryAssert(actual.getPrevious()));
        return myself;
    }

    public SELF isDeleted() {
        isNotNull();
        if (!actual.isDeleted()) {
            failWithMessage("Expected response to be marked deleted but it was not");
        }
        return myself;
    }

    public SELF isNotDeleted() {
        isNotNull();
        if (actual.isDeleted()) {
            failWithMessage("Expected response not to be marked deleted but it was");
        }
        return myself;
    }
}
