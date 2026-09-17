package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpSiteTaglineBlock;

/**
 * Saved-content adapter for {@code core/site-tagline}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:site-tagline /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SiteTaglineBlockAdapter extends SavedBlockAdapter<WpSiteTaglineBlock> {
    /**
     * Creates an adapter for {@code core/site-tagline}.
     */
    public SiteTaglineBlockAdapter() {
        super("core/site-tagline", WpSiteTaglineBlock.class, WpSiteTaglineBlock::new);
    }
}
