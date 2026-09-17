package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpVideoBlock;

/**
 * Saved-content adapter for {@code core/video}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:video -->
 * <figure class="wp-block-video"><video controls src="https://example.com/video.mp4"></video></figure>
 * <!-- /wp:video -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class VideoBlockAdapter extends SavedBlockAdapter<WpVideoBlock> {
    /**
     * Creates an adapter for {@code core/video}.
     */
    public VideoBlockAdapter() {
        super("core/video", WpVideoBlock.class, WpVideoBlock::new);
    }
}
