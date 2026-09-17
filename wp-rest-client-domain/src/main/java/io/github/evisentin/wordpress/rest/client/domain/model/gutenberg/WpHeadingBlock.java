package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.Map;

/**
 * Typed editing model for heading blocks. Conversion to saved markup requires a compatible {@link WpBlockAdapter}.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:heading {"level":3} -->
 * <h3 class="wp-block-heading">Introduction</h3>
 * <!-- /wp:heading -->
 * }</pre>
 *
 * @param contentHtml
 *         inner rich-text HTML
 * @param level
 *         heading level from 1 through 6
 * @param attributes
 *         remaining comment attributes; level is represented by the dedicated field
 * @param source
 *         original block for preserving saved markup, or null for a new block
 */
public record WpHeadingBlock(String contentHtml, int level, Map<String, Object> attributes, WpBlock source) {
    /**
     * Creates a new block without previously saved markup.
     */
    public WpHeadingBlock(String contentHtml, int level, Map<String, Object> attributes) {
        this(contentHtml, level, attributes, null);
    }
}
