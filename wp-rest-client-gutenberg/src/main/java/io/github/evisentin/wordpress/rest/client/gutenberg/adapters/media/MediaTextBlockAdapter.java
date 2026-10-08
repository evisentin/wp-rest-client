package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpMediaTextBlock;

/**
 * Saved-content adapter for {@code core/media-text}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:media-text {"mediaUrl":"https://example.com/photo.jpg","mediaType":"image"} -->
 * <div class="wp-block-media-text is-stacked-on-mobile"><figure class="wp-block-media-text__media"><img src="https://example.com/photo.jpg" alt="Landscape"/></figure><div class="wp-block-media-text__content">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div></div>
 * <!-- /wp:media-text -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class MediaTextBlockAdapter extends SavedBlockAdapter<WpMediaTextBlock> {
    /**
     * Creates an adapter for {@code core/media-text}.
     */
    public MediaTextBlockAdapter() {
        super("core/media-text", WpMediaTextBlock.class, WpMediaTextBlock::new);
    }
}
