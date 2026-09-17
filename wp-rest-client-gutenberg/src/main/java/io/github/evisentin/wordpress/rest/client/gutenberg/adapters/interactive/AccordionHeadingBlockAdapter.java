package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.interactive;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.interactive.WpAccordionHeadingBlock;

/**
 * Saved-content adapter for {@code core/accordion-heading}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:accordion-heading -->
 * <h3 class="wp-block-accordion-heading"><button class="wp-block-accordion-heading__toggle"><span class="wp-block-accordion-heading__toggle-title">Question</span><span class="wp-block-accordion-heading__toggle-icon" aria-hidden="true">+</span></button></h3>
 * <!-- /wp:accordion-heading -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AccordionHeadingBlockAdapter extends SavedBlockAdapter<WpAccordionHeadingBlock> {
    /**
     * Creates an adapter for {@code core/accordion-heading}.
     */
    public AccordionHeadingBlockAdapter() {
        super("core/accordion-heading", WpAccordionHeadingBlock.class, WpAccordionHeadingBlock::new);
    }
}
