package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQuoteBlock;

/**
 * Saved-content adapter for {@code core/quote}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:quote -->
 * <blockquote class="wp-block-quote">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * <cite>Author</cite></blockquote>
 * <!-- /wp:quote -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QuoteBlockAdapter extends SavedBlockAdapter<WpQuoteBlock> {
    /**
     * Creates an adapter for {@code core/quote}.
     */
    public QuoteBlockAdapter() {
        super("core/quote", WpQuoteBlock.class, WpQuoteBlock::new);
    }
}
