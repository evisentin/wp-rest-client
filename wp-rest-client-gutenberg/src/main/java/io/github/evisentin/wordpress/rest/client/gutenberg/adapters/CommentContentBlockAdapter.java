package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpCommentContentBlock;

/**
 * Saved-content adapter for {@code core/comment-content}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comment-content /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentContentBlockAdapter extends SavedBlockAdapter<WpCommentContentBlock> {
    /**
     * Creates an adapter for {@code core/comment-content}.
     */
    public CommentContentBlockAdapter() {
        super("core/comment-content", WpCommentContentBlock.class, WpCommentContentBlock::new);
    }
}
