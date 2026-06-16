package io.github.evisentin.wordpress.client.domain.model.query;

import lombok.Data;

/**
 * Represents pagination parameters with a page number and page size.
 *
 * <p>This class is immutable and validates that both {@code pageNumber}
 * and {@code pageSize} are greater than zero.</p>
 */
@Data
public class WpPagingQuery {

    /**
     * The page number to retrieve (1-based index).
     */
    private final int pageNumber;

    /**
     * The number of items per page.
     */
    private final int pageSize;

    /**
     * Creates a new {@code WpPagingQuery} instance.
     *
     * @param pageNumber
     *         the page number (must be greater than 0)
     * @param pageSize
     *         the number of items per page (must be greater than 0)
     *
     * @throws IllegalArgumentException
     *         if {@code pageNumber < 1} or {@code pageSize < 1}
     */
    public WpPagingQuery(int pageNumber, int pageSize) {
        if (pageNumber < 1)
            throw new IllegalArgumentException("Page number must be greater than 0");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page size must be greater than 0");

        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Creates a new {@code WpPagingQuery} instance.
     *
     * @param pageNumber
     *         the page number (must be greater than 0)
     * @param pageSize
     *         the number of items per page (must be greater than 0)
     *
     * @return a new {@code WpPagingQuery} instance
     */
    public static WpPagingQuery of(int pageNumber, int pageSize) {
        return new WpPagingQuery(pageNumber, pageSize);
    }
}
