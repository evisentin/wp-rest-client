package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentsTitleBlock;

/**
 * Saved-content adapter for {@code core/comments-title}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-title {"level":2} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentsTitleBlockAdapter extends SavedBlockAdapter<WpCommentsTitleBlock> {
    /**
     * Creates an adapter for {@code core/comments-title}.
     */
    public CommentsTitleBlockAdapter() {
        super("core/comments-title", WpCommentsTitleBlock.class, WpCommentsTitleBlock::new);
    }
}
