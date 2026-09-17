package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpNextpageBlock;

/**
 * Saved-content adapter for {@code core/nextpage}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:nextpage /-->
 * }</pre>
 * <p>This example uses the explicit block form accepted by this adapter.
 * WordPress may store this content without block delimiters, for example {@code <!--nextpage-->}; the codec preserves
 * that form as literal HTML.</p>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class NextpageBlockAdapter extends SavedBlockAdapter<WpNextpageBlock> {
    /**
     * Creates an adapter for {@code core/nextpage}.
     */
    public NextpageBlockAdapter() {
        super("core/nextpage", WpNextpageBlock.class, WpNextpageBlock::new);
    }
}
