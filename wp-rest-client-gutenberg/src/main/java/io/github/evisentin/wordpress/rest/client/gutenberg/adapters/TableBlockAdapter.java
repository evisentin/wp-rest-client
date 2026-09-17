package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTableBlock;

/**
 * Saved-content adapter for {@code core/table}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:table -->
 * <figure class="wp-block-table"><table><tbody><tr><td>Name</td><td>Value</td></tr></tbody></table></figure>
 * <!-- /wp:table -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TableBlockAdapter extends SavedBlockAdapter<WpTableBlock> {
    /**
     * Creates an adapter for {@code core/table}.
     */
    public TableBlockAdapter() {
        super("core/table", WpTableBlock.class, WpTableBlock::new);
    }
}
