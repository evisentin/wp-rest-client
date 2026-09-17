package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpQueryPaginationPreviousBlock;

/**
 * Saved-content adapter for {@code core/query-pagination-previous}; preserves markup without running its save
 * function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-pagination-previous /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryPaginationPreviousBlockAdapter extends SavedBlockAdapter<WpQueryPaginationPreviousBlock> {
    /**
     * Creates an adapter for {@code core/query-pagination-previous}.
     */
    public QueryPaginationPreviousBlockAdapter() {
        super("core/query-pagination-previous", WpQueryPaginationPreviousBlock.class, WpQueryPaginationPreviousBlock::new);
    }
}
