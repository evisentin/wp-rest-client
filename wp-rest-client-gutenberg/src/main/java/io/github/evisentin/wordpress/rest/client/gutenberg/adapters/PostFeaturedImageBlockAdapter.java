package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPostFeaturedImageBlock;

/**
 * Saved-content adapter for {@code core/post-featured-image}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-featured-image {"isLink":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostFeaturedImageBlockAdapter extends SavedBlockAdapter<WpPostFeaturedImageBlock> {
    /**
     * Creates an adapter for {@code core/post-featured-image}.
     */
    public PostFeaturedImageBlockAdapter() {
        super("core/post-featured-image", WpPostFeaturedImageBlock.class, WpPostFeaturedImageBlock::new);
    }
}
