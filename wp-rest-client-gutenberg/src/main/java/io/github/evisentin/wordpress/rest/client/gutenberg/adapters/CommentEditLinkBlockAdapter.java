package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentEditLinkBlock;

/**
 * Saved-content adapter for {@code core/comment-edit-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comment-edit-link /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentEditLinkBlockAdapter extends SavedBlockAdapter<WpCommentEditLinkBlock> {
    /**
     * Creates an adapter for {@code core/comment-edit-link}.
     */
    public CommentEditLinkBlockAdapter() {
        super("core/comment-edit-link", WpCommentEditLinkBlock.class, WpCommentEditLinkBlock::new);
    }
}
