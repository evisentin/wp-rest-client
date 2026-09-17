package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpAccordionItemBlock;

/**
 * Saved-content adapter for {@code core/accordion-item}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
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
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AccordionItemBlockAdapter extends SavedBlockAdapter<WpAccordionItemBlock> {
    /**
     * Creates an adapter for {@code core/accordion-item}.
     */
    public AccordionItemBlockAdapter() {
        super("core/accordion-item", WpAccordionItemBlock.class, WpAccordionItemBlock::new);
    }
}
