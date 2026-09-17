package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPostTermsBlock;

/**
 * Saved-content adapter for {@code core/post-terms}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-terms {"term":"category"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostTermsBlockAdapter extends SavedBlockAdapter<WpPostTermsBlock> {
    /**
     * Creates an adapter for {@code core/post-terms}.
     */
    public PostTermsBlockAdapter() {
        super("core/post-terms", WpPostTermsBlock.class, WpPostTermsBlock::new);
    }
}
