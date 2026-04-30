package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.model.WpPagedResponse;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.List;

public abstract class AbstractWpPagedResponseAssert<S extends AbstractWpPagedResponseAssert<S, T>, T>
        extends AbstractObjectAssert<S, WpPagedResponse<T>> {

    protected AbstractWpPagedResponseAssert(final WpPagedResponse<T> actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S doesNotHaveNextPage() {
        isNotNull();
        if (actual.hasNextPage()) {
            failWithMessage("Expected page to have no next page but it did");
        }
        return myself;
    }

    public S hasItemCount(final int expected) {
        isNotNull();
        final int actualCount = actual.getItems() == null ? 0 : actual.getItems().size();
        if (actualCount != expected) {
            failWithMessage("Expected item count to be <%s> but was <%s>", expected, actualCount);
        }
        return myself;
    }

    public S hasItems(final List<T> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getItems(), expected)) {
            failWithMessage("Expected page items to be <%s> but was <%s>", expected, actual.getItems());
        }
        return myself;
    }

    public S hasItemsPerPage(final int expected) {
        isNotNull();
        if (actual.getItemsPerPage() != expected) {
            failWithMessage("Expected itemsPerPage to be <%s> but was <%s>", expected, actual.getItemsPerPage());
        }
        return myself;
    }

    public S hasNextPage() {
        isNotNull();
        if (!actual.hasNextPage()) {
            failWithMessage("Expected page to have a next page but it did not");
        }
        return myself;
    }

    public S hasPageNumber(final int expected) {
        isNotNull();
        if (actual.getPageNumber() != expected) {
            failWithMessage("Expected pageNumber to be <%s> but was <%s>", expected, actual.getPageNumber());
        }
        return myself;
    }

    public S hasTotalItems(final int expected) {
        isNotNull();
        if (actual.getTotalItems() != expected) {
            failWithMessage("Expected totalItems to be <%s> but was <%s>", expected, actual.getTotalItems());
        }
        return myself;
    }

    public S hasTotalPages(final int expected) {
        isNotNull();
        if (actual.getTotalPages() != expected) {
            failWithMessage("Expected totalPages to be <%s> but was <%s>", expected, actual.getTotalPages());
        }
        return myself;
    }

    public S isEmptyPage() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage("Expected page to be empty but contained <%s> items", actual.getItems().size());
        }
        return myself;
    }

    public S isNotEmptyPage() {
        isNotNull();
        if (actual.isEmpty()) {
            failWithMessage("Expected page not to be empty but it was");
        }
        return myself;
    }
}
