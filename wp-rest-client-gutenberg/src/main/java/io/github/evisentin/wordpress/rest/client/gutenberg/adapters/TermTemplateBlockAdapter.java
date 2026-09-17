package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpTermTemplateBlock;

/**
 * Saved-content adapter for {@code core/term-template}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:term-template -->
 * <!-- wp:term-name {"level":2,"isLink":true} /-->
 * <!-- wp:term-description /-->
 * <!-- /wp:term-template -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TermTemplateBlockAdapter extends SavedBlockAdapter<WpTermTemplateBlock> {
    /**
     * Creates an adapter for {@code core/term-template}.
     */
    public TermTemplateBlockAdapter() {
        super("core/term-template", WpTermTemplateBlock.class, WpTermTemplateBlock::new);
    }
}
