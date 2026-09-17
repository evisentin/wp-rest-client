package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.navigation;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.navigation.WpNavigationOverlayCloseBlock;

/**
 * Saved-content adapter for {@code core/navigation-overlay-close}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:navigation-overlay-close {"displayMode":"icon"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class NavigationOverlayCloseBlockAdapter extends SavedBlockAdapter<WpNavigationOverlayCloseBlock> {
    /**
     * Creates an adapter for {@code core/navigation-overlay-close}.
     */
    public NavigationOverlayCloseBlockAdapter() {
        super("core/navigation-overlay-close", WpNavigationOverlayCloseBlock.class, WpNavigationOverlayCloseBlock::new);
    }
}
