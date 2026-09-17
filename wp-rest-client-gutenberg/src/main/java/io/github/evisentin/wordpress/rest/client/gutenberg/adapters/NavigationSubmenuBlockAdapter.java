package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpNavigationSubmenuBlock;

/**
 * Saved-content adapter for {@code core/navigation-submenu}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:navigation-submenu {"label":"Company","url":"https://example.com/company"} -->
 * <!-- wp:navigation-link {"label":"About","url":"https://example.com/about","kind":"custom"} /-->
 * <!-- /wp:navigation-submenu -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class NavigationSubmenuBlockAdapter extends SavedBlockAdapter<WpNavigationSubmenuBlock> {
    /**
     * Creates an adapter for {@code core/navigation-submenu}.
     */
    public NavigationSubmenuBlockAdapter() {
        super("core/navigation-submenu", WpNavigationSubmenuBlock.class, WpNavigationSubmenuBlock::new);
    }
}
