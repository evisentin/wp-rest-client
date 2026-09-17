package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostDateBlock;

/**
 * Saved-content adapter for {@code core/post-date}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-date {"format":"F j, Y"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostDateBlockAdapter extends SavedBlockAdapter<WpPostDateBlock> {
    /**
     * Creates an adapter for {@code core/post-date}.
     */
    public PostDateBlockAdapter() {
        super("core/post-date", WpPostDateBlock.class, WpPostDateBlock::new);
    }
}
