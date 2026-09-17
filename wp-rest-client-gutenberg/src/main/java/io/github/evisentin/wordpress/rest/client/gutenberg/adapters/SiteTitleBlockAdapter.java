package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpSiteTitleBlock;

/**
 * Saved-content adapter for {@code core/site-title}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:site-title {"level":1} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SiteTitleBlockAdapter extends SavedBlockAdapter<WpSiteTitleBlock> {
    /**
     * Creates an adapter for {@code core/site-title}.
     */
    public SiteTitleBlockAdapter() {
        super("core/site-title", WpSiteTitleBlock.class, WpSiteTitleBlock::new);
    }
}
