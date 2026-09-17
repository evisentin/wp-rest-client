package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.interactive;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.interactive.WpTabsBlock;

/**
 * Saved-content adapter for {@code core/tabs}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:tabs -->
 * <!-- wp:tab-list {"tabs":[{"label":"Overview"}]} /-->
 * <!-- wp:tab-panels -->
 * <!-- wp:tab-panel {"label":"Overview"} -->
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * <!-- /wp:tab-panel -->
 * <!-- /wp:tab-panels -->
 * <!-- /wp:tabs -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TabsBlockAdapter extends SavedBlockAdapter<WpTabsBlock> {
    /**
     * Creates an adapter for {@code core/tabs}.
     */
    public TabsBlockAdapter() {
        super("core/tabs", WpTabsBlock.class, WpTabsBlock::new);
    }
}
