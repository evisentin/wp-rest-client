package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpPagedResponse;
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
        final int actualCount = actual.items() == null ? 0 : actual.items().size();
        if (actualCount != expected) {
            failWithMessage("Expected item count to be <%s> but was <%s>", expected, actualCount);
        }
        return myself;
    }

    public S hasItems(final List<T> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.items(), expected)) {
            failWithMessage("Expected page items to be <%s> but was <%s>", expected, actual.items());
        }
        return myself;
    }

    public S hasItemsPerPage(final int expected) {
        isNotNull();
        if (actual.itemsPerPage() != expected) {
            failWithMessage("Expected itemsPerPage to be <%s> but was <%s>", expected, actual.itemsPerPage());
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
        if (actual.pageNumber() != expected) {
            failWithMessage("Expected pageNumber to be <%s> but was <%s>", expected, actual.pageNumber());
        }
        return myself;
    }

    public S hasTotalItems(final int expected) {
        isNotNull();
        if (actual.totalItems() != expected) {
            failWithMessage("Expected totalItems to be <%s> but was <%s>", expected, actual.totalItems());
        }
        return myself;
    }

    public S hasTotalPages(final int expected) {
        isNotNull();
        if (actual.totalPages() != expected) {
            failWithMessage("Expected totalPages to be <%s> but was <%s>", expected, actual.totalPages());
        }
        return myself;
    }

    public S isEmptyPage() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage("Expected page to be empty but contained <%s> items", actual.items().size());
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
