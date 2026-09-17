package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPlaylistBlock;

/**
 * Saved-content adapter for {@code core/playlist}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:playlist -->
 * <!-- wp:playlist-track {"id":42,"src":"https://example.com/audio.mp3","title":"Track one"} /-->
 * <!-- /wp:playlist -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PlaylistBlockAdapter extends SavedBlockAdapter<WpPlaylistBlock> {
    /**
     * Creates an adapter for {@code core/playlist}.
     */
    public PlaylistBlockAdapter() {
        super("core/playlist", WpPlaylistBlock.class, WpPlaylistBlock::new);
    }
}
