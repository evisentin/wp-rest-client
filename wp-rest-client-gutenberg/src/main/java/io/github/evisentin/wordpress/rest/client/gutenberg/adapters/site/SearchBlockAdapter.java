package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.site;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.site.WpSearchBlock;

/**
 * Saved-content adapter for {@code core/search}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:search {"label":"Search","buttonText":"Search"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SearchBlockAdapter extends SavedBlockAdapter<WpSearchBlock> {
    /**
     * Creates an adapter for {@code core/search}.
     */
    public SearchBlockAdapter() {
        super("core/search", WpSearchBlock.class, WpSearchBlock::new);
    }
}
