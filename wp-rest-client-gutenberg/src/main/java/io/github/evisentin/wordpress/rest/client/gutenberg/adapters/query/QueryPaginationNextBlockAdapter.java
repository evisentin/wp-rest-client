package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.query;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.query.WpQueryPaginationNextBlock;

/**
 * Saved-content adapter for {@code core/query-pagination-next}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-pagination-next /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryPaginationNextBlockAdapter extends SavedBlockAdapter<WpQueryPaginationNextBlock> {
    /**
     * Creates an adapter for {@code core/query-pagination-next}.
     */
    public QueryPaginationNextBlockAdapter() {
        super("core/query-pagination-next", WpQueryPaginationNextBlock.class, WpQueryPaginationNextBlock::new);
    }
}
