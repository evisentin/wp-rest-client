package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.post;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.post.WpPostCommentsCountBlock;

/**
 * Saved-content adapter for {@code core/post-comments-count}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-comments-count /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostCommentsCountBlockAdapter extends SavedBlockAdapter<WpPostCommentsCountBlock> {
    /**
     * Creates an adapter for {@code core/post-comments-count}.
     */
    public PostCommentsCountBlockAdapter() {
        super("core/post-comments-count", WpPostCommentsCountBlock.class, WpPostCommentsCountBlock::new);
    }
}
