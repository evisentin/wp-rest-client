package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTabListBlock;

/**
 * Saved-content adapter for {@code core/tab-list}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:tab-list {"tabs":[{"label":"Overview"}]} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TabListBlockAdapter extends SavedBlockAdapter<WpTabListBlock> {
    /**
     * Creates an adapter for {@code core/tab-list}.
     */
    public TabListBlockAdapter() {
        super("core/tab-list", WpTabListBlock.class, WpTabListBlock::new);
    }
}
