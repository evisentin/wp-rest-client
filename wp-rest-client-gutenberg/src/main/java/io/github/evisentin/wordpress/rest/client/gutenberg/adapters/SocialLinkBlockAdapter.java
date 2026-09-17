package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpSocialLinkBlock;

/**
 * Saved-content adapter for {@code core/social-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:social-link {"url":"https://example.com","service":"wordpress"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SocialLinkBlockAdapter extends SavedBlockAdapter<WpSocialLinkBlock> {
    /**
     * Creates an adapter for {@code core/social-link}.
     */
    public SocialLinkBlockAdapter() {
        super("core/social-link", WpSocialLinkBlock.class, WpSocialLinkBlock::new);
    }
}
