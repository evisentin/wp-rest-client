package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpNavigationLinkBlock;

/**
 * Saved-content adapter for {@code core/navigation-link}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:navigation-link {"label":"About","url":"https://example.com/about","kind":"custom"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class NavigationLinkBlockAdapter extends SavedBlockAdapter<WpNavigationLinkBlock> {
    /**
     * Creates an adapter for {@code core/navigation-link}.
     */
    public NavigationLinkBlockAdapter() {
        super("core/navigation-link", WpNavigationLinkBlock.class, WpNavigationLinkBlock::new);
    }
}
