package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTextColumnsBlock;

/**
 * Saved-content adapter for {@code core/text-columns}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:text-columns {"columns":2} -->
 * <div class="wp-block-text-columns alignnone columns-2"><div class="wp-block-column"><p>Left</p></div><div class="wp-block-column"><p>Right</p></div></div>
 * <!-- /wp:text-columns -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TextColumnsBlockAdapter extends SavedBlockAdapter<WpTextColumnsBlock> {
    /**
     * Creates an adapter for {@code core/text-columns}.
     */
    public TextColumnsBlockAdapter() {
        super("core/text-columns", WpTextColumnsBlock.class, WpTextColumnsBlock::new);
    }
}
