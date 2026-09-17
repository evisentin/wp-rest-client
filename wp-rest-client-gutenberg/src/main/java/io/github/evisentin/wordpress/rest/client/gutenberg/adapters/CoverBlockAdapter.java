package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCoverBlock;

/**
 * Saved-content adapter for {@code core/cover}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:cover {"url":"https://example.com/cover.jpg","alt":"Landscape"} -->
 * <div class="wp-block-cover"><span aria-hidden="true" class="wp-block-cover__background has-background-dim"></span><img class="wp-block-cover__image-background" alt="Landscape" src="https://example.com/cover.jpg" data-object-fit="cover"/><div class="wp-block-cover__inner-container">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div></div>
 * <!-- /wp:cover -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CoverBlockAdapter extends SavedBlockAdapter<WpCoverBlock> {
    /**
     * Creates an adapter for {@code core/cover}.
     */
    public CoverBlockAdapter() {
        super("core/cover", WpCoverBlock.class, WpCoverBlock::new);
    }
}
