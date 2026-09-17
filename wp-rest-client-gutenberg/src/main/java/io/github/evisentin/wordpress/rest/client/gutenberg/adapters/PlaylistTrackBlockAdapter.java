package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPlaylistTrackBlock;

/**
 * Saved-content adapter for {@code core/playlist-track}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:playlist-track {"id":42,"src":"https://example.com/audio.mp3","title":"Track one"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PlaylistTrackBlockAdapter extends SavedBlockAdapter<WpPlaylistTrackBlock> {
    /**
     * Creates an adapter for {@code core/playlist-track}.
     */
    public PlaylistTrackBlockAdapter() {
        super("core/playlist-track", WpPlaylistTrackBlock.class, WpPlaylistTrackBlock::new);
    }
}
