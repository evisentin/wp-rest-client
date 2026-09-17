package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPageListBlock;

/**
 * Saved-content adapter for {@code core/page-list}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:page-list {"parentPageID":42} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PageListBlockAdapter extends SavedBlockAdapter<WpPageListBlock> {
    /**
     * Creates an adapter for {@code core/page-list}.
     */
    public PageListBlockAdapter() {
        super("core/page-list", WpPageListBlock.class, WpPageListBlock::new);
    }
}
