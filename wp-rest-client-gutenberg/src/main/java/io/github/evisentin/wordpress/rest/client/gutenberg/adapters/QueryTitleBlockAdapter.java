package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpQueryTitleBlock;

/**
 * Saved-content adapter for {@code core/query-title}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-title {"type":"archive","showPrefix":false} /-->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class QueryTitleBlockAdapter extends SavedBlockAdapter<WpQueryTitleBlock> {
    /**
     * Creates an adapter for {@code core/query-title}.
     */
    public QueryTitleBlockAdapter() {
        super("core/query-title", WpQueryTitleBlock.class, WpQueryTitleBlock::new);
    }
}
