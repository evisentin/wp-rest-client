package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.Map;

/**
 * Typed editing model for paragraph blocks. Conversion to saved markup requires a compatible {@link WpBlockAdapter}.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 *
 * @param contentHtml
 *         inner rich-text HTML, including inline formatting
 * @param attributes
 *         comment attributes, including unrecognized options
 * @param source
 *         original block for preserving saved markup, or null for a new block
 */
public record WpParagraphBlock(String contentHtml, Map<String, Object> attributes, WpBlock source) {
    /**
     * Creates a new block without previously saved markup.
     */
    public WpParagraphBlock(String contentHtml, Map<String, Object> attributes) {
        this(contentHtml, attributes, null);
    }
}
