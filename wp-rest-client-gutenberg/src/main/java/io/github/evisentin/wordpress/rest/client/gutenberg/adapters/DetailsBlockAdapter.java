package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpDetailsBlock;

/**
 * Saved-content adapter for {@code core/details}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:details -->
 * <details class="wp-block-details"><summary>More information</summary>
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </details>
 * <!-- /wp:details -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class DetailsBlockAdapter extends SavedBlockAdapter<WpDetailsBlock> {
    /**
     * Creates an adapter for {@code core/details}.
     */
    public DetailsBlockAdapter() {
        super("core/details", WpDetailsBlock.class, WpDetailsBlock::new);
    }
}
