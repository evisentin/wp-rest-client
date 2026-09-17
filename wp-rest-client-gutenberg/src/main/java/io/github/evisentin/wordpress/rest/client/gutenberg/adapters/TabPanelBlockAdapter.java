package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTabPanelBlock;

/**
 * Saved-content adapter for {@code core/tab-panel}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:tab-panel -->
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * <!-- /wp:tab-panel -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TabPanelBlockAdapter extends SavedBlockAdapter<WpTabPanelBlock> {
    /**
     * Creates an adapter for {@code core/tab-panel}.
     */
    public TabPanelBlockAdapter() {
        super("core/tab-panel", WpTabPanelBlock.class, WpTabPanelBlock::new);
    }
}
