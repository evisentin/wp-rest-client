package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.query;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.query.WpTermDescriptionBlock;

/**
 * Saved-content adapter for {@code core/term-description}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:term-description /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TermDescriptionBlockAdapter extends SavedBlockAdapter<WpTermDescriptionBlock> {
    /**
     * Creates an adapter for {@code core/term-description}.
     */
    public TermDescriptionBlockAdapter() {
        super("core/term-description", WpTermDescriptionBlock.class, WpTermDescriptionBlock::new);
    }
}
