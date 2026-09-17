package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpSocialLinksBlock;

/**
 * Saved-content adapter for {@code core/social-links}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:social-links -->
 * <ul class="wp-block-social-links">
 * <!-- wp:social-link {"url":"https://example.com","service":"wordpress"} /-->
 * </ul>
 * <!-- /wp:social-links -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SocialLinksBlockAdapter extends SavedBlockAdapter<WpSocialLinksBlock> {
    /**
     * Creates an adapter for {@code core/social-links}.
     */
    public SocialLinksBlockAdapter() {
        super("core/social-links", WpSocialLinksBlock.class, WpSocialLinksBlock::new);
    }
}
