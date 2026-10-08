package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpGalleryBlock;

/**
 * Saved-content adapter for {@code core/gallery}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:gallery -->
 * <figure class="wp-block-gallery has-nested-images columns-default is-cropped">
 * <!-- wp:image {"id":42} -->
 * <figure class="wp-block-image"><img src="https://example.com/photo.jpg" alt="A mountain" class="wp-image-42"/></figure>
 * <!-- /wp:image -->
 * </figure>
 * <!-- /wp:gallery -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class GalleryBlockAdapter extends SavedBlockAdapter<WpGalleryBlock> {
    /**
     * Creates an adapter for {@code core/gallery}.
     */
    public GalleryBlockAdapter() {
        super("core/gallery", WpGalleryBlock.class, WpGalleryBlock::new);
    }
}
