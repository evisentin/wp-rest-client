package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.comments;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.comments.WpCommentsPaginationNextBlock;

/**
 * Saved-content adapter for {@code core/comments-pagination-next}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination-next /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsPaginationNextBlockAdapter extends SavedBlockAdapter<WpCommentsPaginationNextBlock> {
    /**
     * Creates an adapter for {@code core/comments-pagination-next}.
     */
    public CommentsPaginationNextBlockAdapter() {
        super("core/comments-pagination-next", WpCommentsPaginationNextBlock.class, WpCommentsPaginationNextBlock::new);
    }
}
