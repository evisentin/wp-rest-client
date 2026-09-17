package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPostExcerptBlock;

/**
 * Saved-content adapter for {@code core/post-excerpt}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-excerpt {"excerptLength":30} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostExcerptBlockAdapter extends SavedBlockAdapter<WpPostExcerptBlock> {
    /**
     * Creates an adapter for {@code core/post-excerpt}.
     */
    public PostExcerptBlockAdapter() {
        super("core/post-excerpt", WpPostExcerptBlock.class, WpPostExcerptBlock::new);
    }
}
