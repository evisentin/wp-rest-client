package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.navigation;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.navigation.WpNavigationBlock;

/**
 * Saved-content adapter for {@code core/navigation}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:navigation {"ref":42} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class NavigationBlockAdapter extends SavedBlockAdapter<WpNavigationBlock> {
    /**
     * Creates an adapter for {@code core/navigation}.
     */
    public NavigationBlockAdapter() {
        super("core/navigation", WpNavigationBlock.class, WpNavigationBlock::new);
    }
}
