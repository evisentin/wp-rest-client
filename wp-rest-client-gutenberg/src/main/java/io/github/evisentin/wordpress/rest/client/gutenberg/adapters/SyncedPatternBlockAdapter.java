package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpSyncedPatternBlock;

/**
 * Saved-content adapter for {@code core/block}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:block {"ref":42} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SyncedPatternBlockAdapter extends SavedBlockAdapter<WpSyncedPatternBlock> {
    /**
     * Creates an adapter for {@code core/block}.
     */
    public SyncedPatternBlockAdapter() {
        super("core/block", WpSyncedPatternBlock.class, WpSyncedPatternBlock::new);
    }
}
