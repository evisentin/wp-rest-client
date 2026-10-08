package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.comments;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.comments.WpCommentsPaginationNumbersBlock;

/**
 * Saved-content adapter for {@code core/comments-pagination-numbers}; preserves markup without running its save
 * function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination-numbers /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsPaginationNumbersBlockAdapter extends SavedBlockAdapter<WpCommentsPaginationNumbersBlock> {
    /**
     * Creates an adapter for {@code core/comments-pagination-numbers}.
     */
    public CommentsPaginationNumbersBlockAdapter() {
        super("core/comments-pagination-numbers", WpCommentsPaginationNumbersBlock.class, WpCommentsPaginationNumbersBlock::new);
    }
}
