package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTabPanelsBlock;

/**
 * Saved-content adapter for {@code core/tab-panels}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:tab-panels -->
 * <!-- wp:tab-panel {"label":"Overview"} -->
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * <!-- /wp:tab-panel -->
 * <!-- /wp:tab-panels -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TabPanelsBlockAdapter extends SavedBlockAdapter<WpTabPanelsBlock> {
    /**
     * Creates an adapter for {@code core/tab-panels}.
     */
    public TabPanelsBlockAdapter() {
        super("core/tab-panels", WpTabPanelsBlock.class, WpTabPanelsBlock::new);
    }
}
