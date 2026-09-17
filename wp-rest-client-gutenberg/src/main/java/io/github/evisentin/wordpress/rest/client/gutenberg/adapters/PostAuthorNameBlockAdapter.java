package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostAuthorNameBlock;

/**
 * Saved-content adapter for {@code core/post-author-name}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-author-name {"isLink":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostAuthorNameBlockAdapter extends SavedBlockAdapter<WpPostAuthorNameBlock> {
    /**
     * Creates an adapter for {@code core/post-author-name}.
     */
    public PostAuthorNameBlockAdapter() {
        super("core/post-author-name", WpPostAuthorNameBlock.class, WpPostAuthorNameBlock::new);
    }
}
