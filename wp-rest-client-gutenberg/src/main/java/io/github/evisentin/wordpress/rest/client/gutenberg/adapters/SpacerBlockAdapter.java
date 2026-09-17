package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpSpacerBlock;

/**
 * Saved-content adapter for {@code core/spacer}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:spacer {"height":"40px"} -->
 * <div style="height:40px" aria-hidden="true" class="wp-block-spacer"></div>
 * <!-- /wp:spacer -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class SpacerBlockAdapter extends SavedBlockAdapter<WpSpacerBlock> {
    /**
     * Creates an adapter for {@code core/spacer}.
     */
    public SpacerBlockAdapter() {
        super("core/spacer", WpSpacerBlock.class, WpSpacerBlock::new);
    }
}
