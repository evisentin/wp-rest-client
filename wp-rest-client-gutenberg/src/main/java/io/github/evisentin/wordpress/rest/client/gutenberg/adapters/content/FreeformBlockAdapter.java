package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpFreeformBlock;

/**
 * Saved-content adapter for {@code core/freeform}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:freeform -->
 * <p>Classic editor content.</p>
 * <!-- /wp:freeform -->
 * }</pre>
 * <p>This example uses the explicit block form accepted by this adapter.
 * WordPress may store this content without block delimiters, for example {@code <p>Classic editor content.</p>}; the
 * codec preserves that form as literal HTML.</p>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class FreeformBlockAdapter extends SavedBlockAdapter<WpFreeformBlock> {
    /**
     * Creates an adapter for {@code core/freeform}.
     */
    public FreeformBlockAdapter() {
        super("core/freeform", WpFreeformBlock.class, WpFreeformBlock::new);
    }
}
