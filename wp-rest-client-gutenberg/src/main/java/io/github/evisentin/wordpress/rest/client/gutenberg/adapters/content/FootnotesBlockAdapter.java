package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpFootnotesBlock;

/**
 * Saved-content adapter for {@code core/footnotes}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:footnotes /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class FootnotesBlockAdapter extends SavedBlockAdapter<WpFootnotesBlock> {
    /**
     * Creates an adapter for {@code core/footnotes}.
     */
    public FootnotesBlockAdapter() {
        super("core/footnotes", WpFootnotesBlock.class, WpFootnotesBlock::new);
    }
}
