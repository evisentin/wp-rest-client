package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpEmbedBlock;

/**
 * Saved-content adapter for {@code core/embed}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:embed {"url":"https://www.youtube.com/watch?v=example","type":"video","providerNameSlug":"youtube"} -->
 * <figure class="wp-block-embed is-type-video is-provider-youtube wp-block-embed-youtube"><div class="wp-block-embed__wrapper">
 * https://www.youtube.com/watch?v=example
 * </div></figure>
 * <!-- /wp:embed -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class EmbedBlockAdapter extends SavedBlockAdapter<WpEmbedBlock> {
    /**
     * Creates an adapter for {@code core/embed}.
     */
    public EmbedBlockAdapter() {
        super("core/embed", WpEmbedBlock.class, WpEmbedBlock::new);
    }
}
