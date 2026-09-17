package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostContentBlock;

/**
 * Saved-content adapter for {@code core/post-content}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-content {"layout":{"type":"constrained"}} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostContentBlockAdapter extends SavedBlockAdapter<WpPostContentBlock> {
    /**
     * Creates an adapter for {@code core/post-content}.
     */
    public PostContentBlockAdapter() {
        super("core/post-content", WpPostContentBlock.class, WpPostContentBlock::new);
    }
}
