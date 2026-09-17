package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpArchivesBlock;

/**
 * Saved-content adapter for {@code core/archives}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:archives {"showPostCounts":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ArchivesBlockAdapter extends SavedBlockAdapter<WpArchivesBlock> {
    /**
     * Creates an adapter for {@code core/archives}.
     */
    public ArchivesBlockAdapter() {
        super("core/archives", WpArchivesBlock.class, WpArchivesBlock::new);
    }
}
