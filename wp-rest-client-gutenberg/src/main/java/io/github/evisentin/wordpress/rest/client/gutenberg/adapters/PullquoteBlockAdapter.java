package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPullquoteBlock;

/**
 * Saved-content adapter for {@code core/pullquote}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:pullquote -->
 * <figure class="wp-block-pullquote"><blockquote><p>A memorable thought.</p><cite>Author</cite></blockquote></figure>
 * <!-- /wp:pullquote -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PullquoteBlockAdapter extends SavedBlockAdapter<WpPullquoteBlock> {
    /**
     * Creates an adapter for {@code core/pullquote}.
     */
    public PullquoteBlockAdapter() {
        super("core/pullquote", WpPullquoteBlock.class, WpPullquoteBlock::new);
    }
}
