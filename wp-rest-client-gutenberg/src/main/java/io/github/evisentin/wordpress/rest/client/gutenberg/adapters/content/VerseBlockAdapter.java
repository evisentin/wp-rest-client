package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpVerseBlock;

/**
 * Saved-content adapter for {@code core/verse}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:verse -->
 * <pre class="wp-block-verse">The morning sun
 * Across the sea</pre>
 * <!-- /wp:verse -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class VerseBlockAdapter extends SavedBlockAdapter<WpVerseBlock> {
    /**
     * Creates an adapter for {@code core/verse}.
     */
    public VerseBlockAdapter() {
        super("core/verse", WpVerseBlock.class, WpVerseBlock::new);
    }
}
