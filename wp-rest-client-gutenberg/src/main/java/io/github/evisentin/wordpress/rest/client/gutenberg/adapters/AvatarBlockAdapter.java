package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpAvatarBlock;

/**
 * Saved-content adapter for {@code core/avatar}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:avatar {"userId":1,"size":48} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class AvatarBlockAdapter extends SavedBlockAdapter<WpAvatarBlock> {
    /**
     * Creates an adapter for {@code core/avatar}.
     */
    public AvatarBlockAdapter() {
        super("core/avatar", WpAvatarBlock.class, WpAvatarBlock::new);
    }
}
