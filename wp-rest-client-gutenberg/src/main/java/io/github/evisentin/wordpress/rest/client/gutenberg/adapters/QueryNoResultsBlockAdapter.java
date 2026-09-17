package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQueryNoResultsBlock;

/**
 * Saved-content adapter for {@code core/query-no-results}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-no-results -->
 * <!-- wp:paragraph -->
 * <p>No posts found.</p>
 * <!-- /wp:paragraph -->
 * <!-- /wp:query-no-results -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryNoResultsBlockAdapter extends SavedBlockAdapter<WpQueryNoResultsBlock> {
    /**
     * Creates an adapter for {@code core/query-no-results}.
     */
    public QueryNoResultsBlockAdapter() {
        super("core/query-no-results", WpQueryNoResultsBlock.class, WpQueryNoResultsBlock::new);
    }
}
