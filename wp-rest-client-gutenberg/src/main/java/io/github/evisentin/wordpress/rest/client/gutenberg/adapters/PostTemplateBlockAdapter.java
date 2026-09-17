package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostTemplateBlock;

/**
 * Saved-content adapter for {@code core/post-template}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-template -->
 * <!-- wp:post-title {"level":2,"isLink":true} /-->
 * <!-- wp:post-excerpt {"excerptLength":30} /-->
 * <!-- /wp:post-template -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostTemplateBlockAdapter extends SavedBlockAdapter<WpPostTemplateBlock> {
    /**
     * Creates an adapter for {@code core/post-template}.
     */
    public PostTemplateBlockAdapter() {
        super("core/post-template", WpPostTemplateBlock.class, WpPostTemplateBlock::new);
    }
}
