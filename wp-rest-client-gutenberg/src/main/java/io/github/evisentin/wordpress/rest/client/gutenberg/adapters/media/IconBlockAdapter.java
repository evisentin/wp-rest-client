package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpIconBlock;

/**
 * Saved-content adapter for {@code core/icon}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:icon {"icon":"wordpress"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class IconBlockAdapter extends SavedBlockAdapter<WpIconBlock> {
    /**
     * Creates an adapter for {@code core/icon}.
     */
    public IconBlockAdapter() {
        super("core/icon", WpIconBlock.class, WpIconBlock::new);
    }
}
