package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpListBlock;

/**
 * Saved-content adapter for {@code core/list}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:list -->
 * <ul class="wp-block-list">
 * <!-- wp:list-item -->
 * <li>First item</li>
 * <!-- /wp:list-item -->
 * </ul>
 * <!-- /wp:list -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ListBlockAdapter extends SavedBlockAdapter<WpListBlock> {
    /**
     * Creates an adapter for {@code core/list}.
     */
    public ListBlockAdapter() {
        super("core/list", WpListBlock.class, WpListBlock::new);
    }
}
