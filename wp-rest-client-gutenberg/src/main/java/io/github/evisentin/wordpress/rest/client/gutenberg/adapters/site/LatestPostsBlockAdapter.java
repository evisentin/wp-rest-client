package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.site;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.site.WpLatestPostsBlock;

/**
 * Saved-content adapter for {@code core/latest-posts}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:latest-posts {"postsToShow":3} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class LatestPostsBlockAdapter extends SavedBlockAdapter<WpLatestPostsBlock> {
    /**
     * Creates an adapter for {@code core/latest-posts}.
     */
    public LatestPostsBlockAdapter() {
        super("core/latest-posts", WpLatestPostsBlock.class, WpLatestPostsBlock::new);
    }
}
