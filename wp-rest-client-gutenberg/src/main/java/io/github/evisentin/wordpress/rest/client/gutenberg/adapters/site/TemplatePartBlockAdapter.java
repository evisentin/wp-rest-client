package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.site;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.site.WpTemplatePartBlock;

/**
 * Saved-content adapter for {@code core/template-part}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:template-part {"slug":"header","tagName":"header"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TemplatePartBlockAdapter extends SavedBlockAdapter<WpTemplatePartBlock> {
    /**
     * Creates an adapter for {@code core/template-part}.
     */
    public TemplatePartBlockAdapter() {
        super("core/template-part", WpTemplatePartBlock.class, WpTemplatePartBlock::new);
    }
}
