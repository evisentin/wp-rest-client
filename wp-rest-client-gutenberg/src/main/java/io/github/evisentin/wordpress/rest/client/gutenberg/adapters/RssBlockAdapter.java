package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpRssBlock;

/**
 * Saved-content adapter for {@code core/rss}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:rss {"feedURL":"https://example.com/feed/","itemsToShow":3} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class RssBlockAdapter extends SavedBlockAdapter<WpRssBlock> {
    /**
     * Creates an adapter for {@code core/rss}.
     */
    public RssBlockAdapter() {
        super("core/rss", WpRssBlock.class, WpRssBlock::new);
    }
}
