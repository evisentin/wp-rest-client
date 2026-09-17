package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpLoginoutBlock;

/**
 * Saved-content adapter for {@code core/loginout}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:loginout {"displayLoginAsForm":true} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class LoginoutBlockAdapter extends SavedBlockAdapter<WpLoginoutBlock> {
    /**
     * Creates an adapter for {@code core/loginout}.
     */
    public LoginoutBlockAdapter() {
        super("core/loginout", WpLoginoutBlock.class, WpLoginoutBlock::new);
    }
}
