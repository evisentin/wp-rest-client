package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.widgets;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.widgets.WpWidgetGroupBlock;

/**
 * Saved-content adapter for {@code core/widget-group}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:widget-group -->
 * <div class="wp-widget-group__inner-blocks">
 * <!-- wp:legacy-widget {"id":"search-2"} /-->
 * </div>
 * <!-- /wp:widget-group -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class WidgetGroupBlockAdapter extends SavedBlockAdapter<WpWidgetGroupBlock> {
    /**
     * Creates an adapter for {@code core/widget-group}.
     */
    public WidgetGroupBlockAdapter() {
        super("core/widget-group", WpWidgetGroupBlock.class, WpWidgetGroupBlock::new);
    }
}
