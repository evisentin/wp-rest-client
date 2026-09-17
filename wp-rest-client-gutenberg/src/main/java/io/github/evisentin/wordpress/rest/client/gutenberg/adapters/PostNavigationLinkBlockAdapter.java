package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostNavigationLinkBlock;

/**
 * Saved-content adapter for {@code core/post-navigation-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-navigation-link {"type":"previous","showTitle":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostNavigationLinkBlockAdapter extends SavedBlockAdapter<WpPostNavigationLinkBlock> {
    /**
     * Creates an adapter for {@code core/post-navigation-link}.
     */
    public PostNavigationLinkBlockAdapter() {
        super("core/post-navigation-link", WpPostNavigationLinkBlock.class, WpPostNavigationLinkBlock::new);
    }
}
