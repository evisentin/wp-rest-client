package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQueryBlock;

/**
 * Saved-content adapter for {@code core/query}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query -->
 * <!-- wp:post-template -->
 * <!-- wp:post-title {"level":2,"isLink":true} /-->
 * <!-- /wp:post-template -->
 * <!-- /wp:query -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryBlockAdapter extends SavedBlockAdapter<WpQueryBlock> {
    /**
     * Creates an adapter for {@code core/query}.
     */
    public QueryBlockAdapter() {
        super("core/query", WpQueryBlock.class, WpQueryBlock::new);
    }
}
