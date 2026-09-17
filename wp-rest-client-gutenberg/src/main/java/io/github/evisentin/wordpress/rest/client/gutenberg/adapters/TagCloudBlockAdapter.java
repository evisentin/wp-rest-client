package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpTagCloudBlock;

/**
 * Saved-content adapter for {@code core/tag-cloud}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:tag-cloud {"numberOfTags":10} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class TagCloudBlockAdapter extends SavedBlockAdapter<WpTagCloudBlock> {
    /**
     * Creates an adapter for {@code core/tag-cloud}.
     */
    public TagCloudBlockAdapter() {
        super("core/tag-cloud", WpTagCloudBlock.class, WpTagCloudBlock::new);
    }
}
