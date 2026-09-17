package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpFileBlock;

/**
 * Saved-content adapter for {@code core/file}; preserves markup without running its save function.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:file {"showDownloadButton":false,"displayPreview":false} -->
 * <div class="wp-block-file"><a href="https://example.com/report.pdf">Report</a></div>
 * <!-- /wp:file -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class FileBlockAdapter extends SavedBlockAdapter<WpFileBlock> {
    /**
     * Creates an adapter for {@code core/file}.
     */
    public FileBlockAdapter() {
        super("core/file", WpFileBlock.class, WpFileBlock::new);
    }
}
