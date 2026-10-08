package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.layout;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.layout.WpColumnsBlock;

/**
 * Saved-content adapter for {@code core/columns}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:columns -->
 * <div class="wp-block-columns">
 * <!-- wp:column -->
 * <div class="wp-block-column">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:column -->
 * </div>
 * <!-- /wp:columns -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ColumnsBlockAdapter extends SavedBlockAdapter<WpColumnsBlock> {
    /**
     * Creates an adapter for {@code core/columns}.
     */
    public ColumnsBlockAdapter() {
        super("core/columns", WpColumnsBlock.class, WpColumnsBlock::new);
    }
}
