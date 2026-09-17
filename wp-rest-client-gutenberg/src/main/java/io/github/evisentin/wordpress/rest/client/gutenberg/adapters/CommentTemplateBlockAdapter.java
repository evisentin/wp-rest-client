package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCommentTemplateBlock;

/**
 * Saved-content adapter for {@code core/comment-template}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comment-template -->
 * <!-- wp:comment-author-name /-->
 * <!-- wp:comment-content /-->
 * <!-- /wp:comment-template -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CommentTemplateBlockAdapter extends SavedBlockAdapter<WpCommentTemplateBlock> {
    /**
     * Creates an adapter for {@code core/comment-template}.
     */
    public CommentTemplateBlockAdapter() {
        super("core/comment-template", WpCommentTemplateBlock.class, WpCommentTemplateBlock::new);
    }
}
