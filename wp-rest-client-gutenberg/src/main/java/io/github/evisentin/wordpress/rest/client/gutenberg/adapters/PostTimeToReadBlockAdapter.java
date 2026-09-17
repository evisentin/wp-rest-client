package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPostTimeToReadBlock;

/**
 * Saved-content adapter for {@code core/post-time-to-read}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-time-to-read {"displayAsRange":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostTimeToReadBlockAdapter extends SavedBlockAdapter<WpPostTimeToReadBlock> {
    /**
     * Creates an adapter for {@code core/post-time-to-read}.
     */
    public PostTimeToReadBlockAdapter() {
        super("core/post-time-to-read", WpPostTimeToReadBlock.class, WpPostTimeToReadBlock::new);
    }
}
