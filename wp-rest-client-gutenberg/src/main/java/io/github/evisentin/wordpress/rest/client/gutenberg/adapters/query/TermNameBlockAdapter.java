package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.query;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.query.WpTermNameBlock;

/**
 * Saved-content adapter for {@code core/term-name}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:term-name {"level":2,"isLink":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TermNameBlockAdapter extends SavedBlockAdapter<WpTermNameBlock> {
    /**
     * Creates an adapter for {@code core/term-name}.
     */
    public TermNameBlockAdapter() {
        super("core/term-name", WpTermNameBlock.class, WpTermNameBlock::new);
    }
}
