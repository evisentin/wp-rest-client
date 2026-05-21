package io.github.evisentin.wordpress.client.domain.model;

import lombok.Getter;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

/**
 * Generic wrapper representing a paginated response from the WordPress REST API.
 *
 * <p>This class contains the retrieved items along with pagination metadata
 * such as total number of items, total pages, and the current page index.</p>
 *
 * @param <T>
 *         the type of items contained in the response
 */
@Getter
public class WpPagedResponse<T> {

    /**
     * List of items returned for the current page.
     */
    private final List<T> items;

    /**
     * Number of items requested per page.
     */
    private final int itemsPerPage;

    /**
     * Total number of items available across all pages.
     */
    private final int totalItems;

    /**
     * Total number of pages available.
     */
    private final int totalPages;

    /**
     * Current page number (1-based index).
     */
    private final int pageNumber;

    /**
     * Creates a new paged response wrapper.
     *
     * @param items
     *         items returned for the current page; {@code null} is treated as an empty list
     * @param itemsPerPage
     *         number of items requested per page; must be at least 1
     * @param totalItems
     *         total number of items available; must be at least 0
     * @param totalPages
     *         total number of pages available; must be at least 0
     * @param pageNumber
     *         current page number; must be at least 1
     *
     * @throws IllegalArgumentException
     *         if any numeric argument is outside its valid range
     */
    public WpPagedResponse(final List<T> items,
                           final int itemsPerPage,
                           final int totalItems,
                           final int totalPages,
                           final int pageNumber) {

        if (itemsPerPage < 1) throw new IllegalArgumentException("itemsPerPage must be >= 1");
        if (totalItems < 0) throw new IllegalArgumentException("totalItems must be >= 0");
        if (totalPages < 0) throw new IllegalArgumentException("totalPages must be >= 0");
        if (pageNumber < 1) throw new IllegalArgumentException("pageNumber must be >= 1");

        this.items = ofNullable(items).orElse(emptyList());
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
    }

    /**
     * Indicates whether another page of results is available.
     *
     * @return {@code true} if there is a next page, {@code false} otherwise
     */
    public boolean hasNextPage() {
        return pageNumber < totalPages;
    }

    /**
     * Indicates whether the current page contains no items.
     *
     * @return {@code true} if the item list is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
