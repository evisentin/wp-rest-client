package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpAccordionBlock;

/**
 * Saved-content adapter for {@code core/accordion}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:accordion -->
 * <div class="wp-block-accordion">
 * <!-- wp:accordion-item -->
 * <div class="wp-block-accordion-item">
 * <!-- wp:accordion-heading -->
 * <h3 class="wp-block-accordion-heading"><button class="wp-block-accordion-heading__toggle"><span class="wp-block-accordion-heading__toggle-title">Question</span><span class="wp-block-accordion-heading__toggle-icon" aria-hidden="true">+</span></button></h3>
 * <!-- /wp:accordion-heading -->
 * <!-- wp:accordion-panel -->
 * <div role="region" class="wp-block-accordion-panel">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:accordion-panel -->
 * </div>
 * <!-- /wp:accordion-item -->
 * </div>
 * <!-- /wp:accordion -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AccordionBlockAdapter extends SavedBlockAdapter<WpAccordionBlock> {
    /**
     * Creates an adapter for {@code core/accordion}.
     */
    public AccordionBlockAdapter() {
        super("core/accordion", WpAccordionBlock.class, WpAccordionBlock::new);
    }
}
