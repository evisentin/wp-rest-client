package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpButtonBlock;

/**
 * Saved-content adapter for {@code core/button}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:button -->
 * <div class="wp-block-button"><a class="wp-block-button__link wp-element-button" href="https://example.com">Learn more</a></div>
 * <!-- /wp:button -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class ButtonBlockAdapter extends SavedBlockAdapter<WpButtonBlock> {
    /**
     * Creates an adapter for {@code core/button}.
     */
    public ButtonBlockAdapter() {
        super("core/button", WpButtonBlock.class, WpButtonBlock::new);
    }
}
