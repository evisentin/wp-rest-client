package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/media-text}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:media-text {"mediaUrl":"https://example.com/photo.jpg","mediaType":"image"} -->
 * <div class="wp-block-media-text is-stacked-on-mobile"><figure class="wp-block-media-text__media"><img src="https://example.com/photo.jpg" alt="Landscape"/></figure><div class="wp-block-media-text__content">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div></div>
 * <!-- /wp:media-text -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpMediaTextBlock(Map<String, Object> attributes, List<WpContentNode> content,
                               WpBlockSyntax syntax) implements WpSavedBlockModel {
}
