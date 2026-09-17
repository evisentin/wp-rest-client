package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentsPaginationPreviousBlock;

/**
 * Saved-content adapter for {@code core/comments-pagination-previous}; preserves markup without running its save
 * function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination-previous /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsPaginationPreviousBlockAdapter extends SavedBlockAdapter<WpCommentsPaginationPreviousBlock> {
    /**
     * Creates an adapter for {@code core/comments-pagination-previous}.
     */
    public CommentsPaginationPreviousBlockAdapter() {
        super("core/comments-pagination-previous", WpCommentsPaginationPreviousBlock.class, WpCommentsPaginationPreviousBlock::new);
    }
}
