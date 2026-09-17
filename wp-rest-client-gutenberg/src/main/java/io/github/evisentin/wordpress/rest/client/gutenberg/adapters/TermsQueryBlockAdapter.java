package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTermsQueryBlock;

/**
 * Saved-content adapter for {@code core/terms-query}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:terms-query -->
 * <!-- wp:term-template -->
 * <!-- wp:term-name {"level":2,"isLink":true} /-->
 * <!-- /wp:term-template -->
 * <!-- /wp:terms-query -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TermsQueryBlockAdapter extends SavedBlockAdapter<WpTermsQueryBlock> {
    /**
     * Creates an adapter for {@code core/terms-query}.
     */
    public TermsQueryBlockAdapter() {
        super("core/terms-query", WpTermsQueryBlock.class, WpTermsQueryBlock::new);
    }
}
