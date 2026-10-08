package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.interactive;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.interactive.WpAccordionPanelBlock;

/**
 * Saved-content adapter for {@code core/accordion-panel}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:accordion-panel -->
 * <div role="region" class="wp-block-accordion-panel">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:accordion-panel -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AccordionPanelBlockAdapter extends SavedBlockAdapter<WpAccordionPanelBlock> {
    /**
     * Creates an adapter for {@code core/accordion-panel}.
     */
    public AccordionPanelBlockAdapter() {
        super("core/accordion-panel", WpAccordionPanelBlock.class, WpAccordionPanelBlock::new);
    }
}
