package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQueryTotalBlock;

/**
 * Saved-content adapter for {@code core/query-total}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-total {"displayType":"total-results"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryTotalBlockAdapter extends SavedBlockAdapter<WpQueryTotalBlock> {
    /**
     * Creates an adapter for {@code core/query-total}.
     */
    public QueryTotalBlockAdapter() {
        super("core/query-total", WpQueryTotalBlock.class, WpQueryTotalBlock::new);
    }
}
