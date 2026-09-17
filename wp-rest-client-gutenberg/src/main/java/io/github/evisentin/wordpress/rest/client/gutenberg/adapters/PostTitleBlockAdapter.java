package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostTitleBlock;

/**
 * Saved-content adapter for {@code core/post-title}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-title {"level":2,"isLink":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostTitleBlockAdapter extends SavedBlockAdapter<WpPostTitleBlock> {
    /**
     * Creates an adapter for {@code core/post-title}.
     */
    public PostTitleBlockAdapter() {
        super("core/post-title", WpPostTitleBlock.class, WpPostTitleBlock::new);
    }
}
