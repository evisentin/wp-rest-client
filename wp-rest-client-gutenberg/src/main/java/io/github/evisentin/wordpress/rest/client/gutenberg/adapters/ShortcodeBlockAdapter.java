package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpShortcodeBlock;

/**
 * Saved-content adapter for {@code core/shortcode}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:shortcode -->
 * [gallery ids="42,43"]
 * <!-- /wp:shortcode -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ShortcodeBlockAdapter extends SavedBlockAdapter<WpShortcodeBlock> {
    /**
     * Creates an adapter for {@code core/shortcode}.
     */
    public ShortcodeBlockAdapter() {
        super("core/shortcode", WpShortcodeBlock.class, WpShortcodeBlock::new);
    }
}
