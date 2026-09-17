package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPostAuthorBiographyBlock;

/**
 * Saved-content adapter for {@code core/post-author-biography}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:post-author-biography /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PostAuthorBiographyBlockAdapter extends SavedBlockAdapter<WpPostAuthorBiographyBlock> {
    /**
     * Creates an adapter for {@code core/post-author-biography}.
     */
    public PostAuthorBiographyBlockAdapter() {
        super("core/post-author-biography", WpPostAuthorBiographyBlock.class, WpPostAuthorBiographyBlock::new);
    }
}
