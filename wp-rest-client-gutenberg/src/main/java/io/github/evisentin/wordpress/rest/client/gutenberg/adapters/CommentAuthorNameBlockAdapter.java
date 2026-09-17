package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentAuthorNameBlock;

/**
 * Saved-content adapter for {@code core/comment-author-name}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comment-author-name {"isLink":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentAuthorNameBlockAdapter extends SavedBlockAdapter<WpCommentAuthorNameBlock> {
    /**
     * Creates an adapter for {@code core/comment-author-name}.
     */
    public CommentAuthorNameBlockAdapter() {
        super("core/comment-author-name", WpCommentAuthorNameBlock.class, WpCommentAuthorNameBlock::new);
    }
}
