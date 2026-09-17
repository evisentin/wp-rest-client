package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpPatternBlock;

/**
 * Saved-content adapter for {@code core/pattern}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:pattern {"slug":"example/hero"} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class PatternBlockAdapter extends SavedBlockAdapter<WpPatternBlock> {
    /**
     * Creates an adapter for {@code core/pattern}.
     */
    public PatternBlockAdapter() {
        super("core/pattern", WpPatternBlock.class, WpPatternBlock::new);
    }
}
