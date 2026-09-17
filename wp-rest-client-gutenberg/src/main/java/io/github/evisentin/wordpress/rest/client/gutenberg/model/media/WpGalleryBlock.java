package io.github.evisentin.wordpress.rest.client.gutenberg.model.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/gallery}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:gallery -->
 * <figure class="wp-block-gallery has-nested-images columns-default is-cropped">
 * <!-- wp:image {"id":42} -->
 * <figure class="wp-block-image"><img src="https://example.com/photo.jpg" alt="A mountain" class="wp-image-42"/></figure>
 * <!-- /wp:image -->
 * </figure>
 * <!-- /wp:gallery -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpGalleryBlock(Map<String, Object> attributes, List<WpContentNode> content,
                             WpBlockSyntax syntax) implements WpSavedBlockModel {
}
