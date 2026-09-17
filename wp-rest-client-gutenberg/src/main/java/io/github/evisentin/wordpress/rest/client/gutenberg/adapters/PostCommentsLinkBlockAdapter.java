package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPostCommentsLinkBlock;

/**
 * Saved-content adapter for {@code core/post-comments-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-comments-link /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostCommentsLinkBlockAdapter extends SavedBlockAdapter<WpPostCommentsLinkBlock> {
    /**
     * Creates an adapter for {@code core/post-comments-link}.
     */
    public PostCommentsLinkBlockAdapter() {
        super("core/post-comments-link", WpPostCommentsLinkBlock.class, WpPostCommentsLinkBlock::new);
    }
}
