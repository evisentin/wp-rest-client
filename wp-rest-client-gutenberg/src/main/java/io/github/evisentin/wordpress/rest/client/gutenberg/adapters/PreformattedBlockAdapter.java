package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpPreformattedBlock;

/**
 * Saved-content adapter for {@code core/preformatted}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:preformatted -->
 * <pre class="wp-block-preformatted">Line one
 *   Line two</pre>
 * <!-- /wp:preformatted -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PreformattedBlockAdapter extends SavedBlockAdapter<WpPreformattedBlock> {
    /**
     * Creates an adapter for {@code core/preformatted}.
     */
    public PreformattedBlockAdapter() {
        super("core/preformatted", WpPreformattedBlock.class, WpPreformattedBlock::new);
    }
}
