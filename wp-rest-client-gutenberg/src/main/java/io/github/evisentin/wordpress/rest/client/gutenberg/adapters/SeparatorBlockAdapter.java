package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpSeparatorBlock;

/**
 * Saved-content adapter for {@code core/separator}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:separator -->
 * <hr class="wp-block-separator has-alpha-channel-opacity"/>
 * <!-- /wp:separator -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SeparatorBlockAdapter extends SavedBlockAdapter<WpSeparatorBlock> {
    /**
     * Creates an adapter for {@code core/separator}.
     */
    public SeparatorBlockAdapter() {
        super("core/separator", WpSeparatorBlock.class, WpSeparatorBlock::new);
    }
}
