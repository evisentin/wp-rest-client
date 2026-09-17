package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpPageListItemBlock;

/**
 * Saved-content adapter for {@code core/page-list-item}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:page-list-item {"id":42,"label":"About","link":"https://example.com/about"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PageListItemBlockAdapter extends SavedBlockAdapter<WpPageListItemBlock> {
    /**
     * Creates an adapter for {@code core/page-list-item}.
     */
    public PageListItemBlockAdapter() {
        super("core/page-list-item", WpPageListItemBlock.class, WpPageListItemBlock::new);
    }
}
