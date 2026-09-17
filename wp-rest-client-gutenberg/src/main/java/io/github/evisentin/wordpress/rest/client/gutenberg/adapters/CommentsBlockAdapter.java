package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpCommentsBlock;

/**
 * Saved-content adapter for {@code core/comments}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments -->
 * <!-- wp:comments-title /-->
 * <!-- wp:comment-template -->
 * <!-- wp:comment-content /-->
 * <!-- /wp:comment-template -->
 * <!-- /wp:comments -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsBlockAdapter extends SavedBlockAdapter<WpCommentsBlock> {
    /**
     * Creates an adapter for {@code core/comments}.
     */
    public CommentsBlockAdapter() {
        super("core/comments", WpCommentsBlock.class, WpCommentsBlock::new);
    }
}
