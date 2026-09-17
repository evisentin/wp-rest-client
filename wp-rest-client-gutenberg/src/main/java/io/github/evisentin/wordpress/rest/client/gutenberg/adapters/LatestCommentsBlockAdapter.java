package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpLatestCommentsBlock;

/**
 * Saved-content adapter for {@code core/latest-comments}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:latest-comments {"commentsToShow":3} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class LatestCommentsBlockAdapter extends SavedBlockAdapter<WpLatestCommentsBlock> {
    /**
     * Creates an adapter for {@code core/latest-comments}.
     */
    public LatestCommentsBlockAdapter() {
        super("core/latest-comments", WpLatestCommentsBlock.class, WpLatestCommentsBlock::new);
    }
}
