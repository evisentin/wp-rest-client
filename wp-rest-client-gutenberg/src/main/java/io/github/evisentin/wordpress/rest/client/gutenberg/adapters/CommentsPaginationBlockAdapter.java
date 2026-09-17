package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentsPaginationBlock;

/**
 * Saved-content adapter for {@code core/comments-pagination}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination -->
 * <!-- wp:comments-pagination-previous /-->
 * <!-- wp:comments-pagination-numbers /-->
 * <!-- wp:comments-pagination-next /-->
 * <!-- /wp:comments-pagination -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsPaginationBlockAdapter extends SavedBlockAdapter<WpCommentsPaginationBlock> {
    /**
     * Creates an adapter for {@code core/comments-pagination}.
     */
    public CommentsPaginationBlockAdapter() {
        super("core/comments-pagination", WpCommentsPaginationBlock.class, WpCommentsPaginationBlock::new);
    }
}
