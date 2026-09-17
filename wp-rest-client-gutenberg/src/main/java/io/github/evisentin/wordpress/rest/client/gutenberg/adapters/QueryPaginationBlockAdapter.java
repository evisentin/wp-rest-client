package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpQueryPaginationBlock;

/**
 * Saved-content adapter for {@code core/query-pagination}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-pagination -->
 * <!-- wp:query-pagination-previous /-->
 * <!-- wp:query-pagination-numbers /-->
 * <!-- wp:query-pagination-next /-->
 * <!-- /wp:query-pagination -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryPaginationBlockAdapter extends SavedBlockAdapter<WpQueryPaginationBlock> {
    /**
     * Creates an adapter for {@code core/query-pagination}.
     */
    public QueryPaginationBlockAdapter() {
        super("core/query-pagination", WpQueryPaginationBlock.class, WpQueryPaginationBlock::new);
    }
}
