package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostCommentsFormBlock;

/**
 * Saved-content adapter for {@code core/post-comments-form}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-comments-form /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostCommentsFormBlockAdapter extends SavedBlockAdapter<WpPostCommentsFormBlock> {
    /**
     * Creates an adapter for {@code core/post-comments-form}.
     */
    public PostCommentsFormBlockAdapter() {
        super("core/post-comments-form", WpPostCommentsFormBlock.class, WpPostCommentsFormBlock::new);
    }
}
