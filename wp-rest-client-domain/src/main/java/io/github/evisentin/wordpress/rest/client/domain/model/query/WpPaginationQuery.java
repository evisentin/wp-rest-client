package io.github.evisentin.wordpress.rest.client.domain.model.query;

/**
 * Represents pagination parameters with a page number and page size.
 *
 * <p>This class is immutable and validates that both {@code pageNumber}
 * and {@code pageSize} are greater than zero.</p>
 *
 * @param pageNumber
 *         The page number to retrieve (1-based index).
 * @param pageSize
 *         The number of items per page.
 */
public record WpPaginationQuery(int pageNumber, int pageSize) {

    /**
     * Creates a new {@code WpPaginationQuery} instance.
     *
     * @param pageNumber
     *         the page number (must be greater than 0)
     * @param pageSize
     *         the number of items per page (must be greater than 0)
     *
     * @throws IllegalArgumentException
     *         if {@code pageNumber < 1} or {@code pageSize < 1}
     */
    public WpPaginationQuery {
        if (pageNumber < 1)
            throw new IllegalArgumentException("Page number must be greater than 0");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page size must be greater than 0");
    }
}
