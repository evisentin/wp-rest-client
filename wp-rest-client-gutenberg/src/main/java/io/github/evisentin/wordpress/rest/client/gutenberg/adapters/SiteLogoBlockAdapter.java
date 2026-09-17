package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpSiteLogoBlock;

/**
 * Saved-content adapter for {@code core/site-logo}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:site-logo {"width":120} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SiteLogoBlockAdapter extends SavedBlockAdapter<WpSiteLogoBlock> {
    /**
     * Creates an adapter for {@code core/site-logo}.
     */
    public SiteLogoBlockAdapter() {
        super("core/site-logo", WpSiteLogoBlock.class, WpSiteLogoBlock::new);
    }
}
