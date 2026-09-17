package io.github.evisentin.wordpress.rest.client.gutenberg.model.layout;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;

import java.util.List;
import java.util.Map;

/**
 * Typed editing model for group blocks. Conversion to saved markup requires a compatible {@link WpBlockAdapter}.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:group -->
 * <div class="wp-block-group">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:group -->
 * }</pre>
 *
 * @param content
 *         ordered container HTML fragments and child blocks
 * @param attributes
 *         container comment attributes, including layout and style options
 */
public record WpGroupBlock(List<WpContentNode> content, Map<String, Object> attributes) {
}
