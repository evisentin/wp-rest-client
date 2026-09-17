package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpMissingBlock;

/**
 * Saved-content adapter for {@code core/missing}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:missing {"originalName":"vendor/example"} -->
 * <div>Content from an unavailable plugin.</div>
 * <!-- /wp:missing -->
 * }</pre>
 * <p>This example uses the explicit block form accepted by this adapter.
 * WordPress may store this content without block delimiters, for example
 * {@code <div>Content from an unavailable plugin.</div>}; the codec preserves that form as literal HTML.</p>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class MissingBlockAdapter extends SavedBlockAdapter<WpMissingBlock> {
    /**
     * Creates an adapter for {@code core/missing}.
     */
    public MissingBlockAdapter() {
        super("core/missing", WpMissingBlock.class, WpMissingBlock::new);
    }
}
