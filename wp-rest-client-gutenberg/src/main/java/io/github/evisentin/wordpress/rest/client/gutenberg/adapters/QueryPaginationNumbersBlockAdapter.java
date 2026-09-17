package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQueryPaginationNumbersBlock;

/**
 * Saved-content adapter for {@code core/query-pagination-numbers}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-pagination-numbers /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryPaginationNumbersBlockAdapter extends SavedBlockAdapter<WpQueryPaginationNumbersBlock> {
    /**
     * Creates an adapter for {@code core/query-pagination-numbers}.
     */
    public QueryPaginationNumbersBlockAdapter() {
        super("core/query-pagination-numbers", WpQueryPaginationNumbersBlock.class, WpQueryPaginationNumbersBlock::new);
    }
}
