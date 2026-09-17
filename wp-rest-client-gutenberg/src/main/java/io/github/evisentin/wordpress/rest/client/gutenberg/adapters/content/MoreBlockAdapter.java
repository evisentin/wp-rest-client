package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpMoreBlock;

/**
 * Saved-content adapter for {@code core/more}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:more {"customText":"Continue reading"} /-->
 * }</pre>
 * <p>This example uses the explicit block form accepted by this adapter.
 * WordPress may store this content without block delimiters, for example {@code <!--more-->}; the codec preserves that
 * form as literal HTML.</p>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class MoreBlockAdapter extends SavedBlockAdapter<WpMoreBlock> {
    /**
     * Creates an adapter for {@code core/more}.
     */
    public MoreBlockAdapter() {
        super("core/more", WpMoreBlock.class, WpMoreBlock::new);
    }
}
