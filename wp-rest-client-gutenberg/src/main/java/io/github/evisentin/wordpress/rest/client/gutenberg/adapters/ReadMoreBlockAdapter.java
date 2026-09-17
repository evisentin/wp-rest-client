package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpReadMoreBlock;

/**
 * Saved-content adapter for {@code core/read-more}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:read-more {"content":"Read more"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ReadMoreBlockAdapter extends SavedBlockAdapter<WpReadMoreBlock> {
    /**
     * Creates an adapter for {@code core/read-more}.
     */
    public ReadMoreBlockAdapter() {
        super("core/read-more", WpReadMoreBlock.class, WpReadMoreBlock::new);
    }
}
