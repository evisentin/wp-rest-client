package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpListItemBlock;

/**
 * Saved-content adapter for {@code core/list-item}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:list-item -->
 * <li>First item</li>
 * <!-- /wp:list-item -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ListItemBlockAdapter extends SavedBlockAdapter<WpListItemBlock> {
    /**
     * Creates an adapter for {@code core/list-item}.
     */
    public ListItemBlockAdapter() {
        super("core/list-item", WpListItemBlock.class, WpListItemBlock::new);
    }
}
