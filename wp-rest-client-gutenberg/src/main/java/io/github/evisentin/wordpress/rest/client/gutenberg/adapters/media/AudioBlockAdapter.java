package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpAudioBlock;

/**
 * Saved-content adapter for {@code core/audio}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:audio -->
 * <figure class="wp-block-audio"><audio controls src="https://example.com/audio.mp3"></audio></figure>
 * <!-- /wp:audio -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AudioBlockAdapter extends SavedBlockAdapter<WpAudioBlock> {
    /**
     * Creates an adapter for {@code core/audio}.
     */
    public AudioBlockAdapter() {
        super("core/audio", WpAudioBlock.class, WpAudioBlock::new);
    }
}
