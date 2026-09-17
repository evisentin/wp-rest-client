package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.site;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.site.WpCategoriesBlock;

/**
 * Saved-content adapter for {@code core/categories}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:categories {"showPostCounts":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CategoriesBlockAdapter extends SavedBlockAdapter<WpCategoriesBlock> {
    /**
     * Creates an adapter for {@code core/categories}.
     */
    public CategoriesBlockAdapter() {
        super("core/categories", WpCategoriesBlock.class, WpCategoriesBlock::new);
    }
}
