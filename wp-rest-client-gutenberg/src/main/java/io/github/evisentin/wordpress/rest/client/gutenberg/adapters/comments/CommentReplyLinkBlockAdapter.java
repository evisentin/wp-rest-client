package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.comments;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.comments.WpCommentReplyLinkBlock;

/**
 * Saved-content adapter for {@code core/comment-reply-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comment-reply-link /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentReplyLinkBlockAdapter extends SavedBlockAdapter<WpCommentReplyLinkBlock> {
    /**
     * Creates an adapter for {@code core/comment-reply-link}.
     */
    public CommentReplyLinkBlockAdapter() {
        super("core/comment-reply-link", WpCommentReplyLinkBlock.class, WpCommentReplyLinkBlock::new);
    }
}
