package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpColumnBlock;

/**
 * Saved-content adapter for {@code core/column}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:column -->
 * <div class="wp-block-column">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:column -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ColumnBlockAdapter extends SavedBlockAdapter<WpColumnBlock> {
    /**
     * Creates an adapter for {@code core/column}.
     */
    public ColumnBlockAdapter() {
        super("core/column", WpColumnBlock.class, WpColumnBlock::new);
    }
}
