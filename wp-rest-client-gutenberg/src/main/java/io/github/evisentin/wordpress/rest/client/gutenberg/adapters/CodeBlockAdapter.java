package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpCodeBlock;

/**
 * Saved-content adapter for {@code core/code}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:code -->
 * <pre class="wp-block-code"><code>System.out.println("Hello");</code></pre>
 * <!-- /wp:code -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class CodeBlockAdapter extends SavedBlockAdapter<WpCodeBlock> {
    /**
     * Creates an adapter for {@code core/code}.
     */
    public CodeBlockAdapter() {
        super("core/code", WpCodeBlock.class, WpCodeBlock::new);
    }
}
