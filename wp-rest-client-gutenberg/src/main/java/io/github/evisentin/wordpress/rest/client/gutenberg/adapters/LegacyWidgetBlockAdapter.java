package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpLegacyWidgetBlock;

/**
 * Saved-content adapter for {@code core/legacy-widget}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:legacy-widget {"id":"search-2"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class LegacyWidgetBlockAdapter extends SavedBlockAdapter<WpLegacyWidgetBlock> {
    /**
     * Creates an adapter for {@code core/legacy-widget}.
     */
    public LegacyWidgetBlockAdapter() {
        super("core/legacy-widget", WpLegacyWidgetBlock.class, WpLegacyWidgetBlock::new);
    }
}
