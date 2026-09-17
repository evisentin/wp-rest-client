package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostAuthorBlock;

/**
 * Saved-content adapter for {@code core/post-author}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-author {"showAvatar":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostAuthorBlockAdapter extends SavedBlockAdapter<WpPostAuthorBlock> {
    /**
     * Creates an adapter for {@code core/post-author}.
     */
    public PostAuthorBlockAdapter() {
        super("core/post-author", WpPostAuthorBlock.class, WpPostAuthorBlock::new);
    }
}
