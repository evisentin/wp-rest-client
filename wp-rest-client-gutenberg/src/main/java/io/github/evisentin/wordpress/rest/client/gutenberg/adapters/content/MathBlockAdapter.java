package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SavedBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpMathBlock;

/**
 * Saved-content adapter for {@code core/math}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:math {"latex":"x"} -->
 * <div class="wp-block-math"><math xmlns="http://www.w3.org/1998/Math/MathML"><mi>x</mi></math></div>
 * <!-- /wp:math -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class MathBlockAdapter extends SavedBlockAdapter<WpMathBlock> {
    /**
     * Creates an adapter for {@code core/math}.
     */
    public MathBlockAdapter() {
        super("core/math", WpMathBlock.class, WpMathBlock::new);
    }
}
