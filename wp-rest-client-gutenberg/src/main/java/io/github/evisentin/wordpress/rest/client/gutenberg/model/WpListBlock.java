package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/list}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:list -->
 * <ul class="wp-block-list">
 * <!-- wp:list-item -->
 * <li>First item</li>
 * <!-- /wp:list-item -->
 * </ul>
 * <!-- /wp:list -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpListBlock(Map<String, Object> attributes, List<WpContentNode> content,
                          WpBlockSyntax syntax) implements WpSavedBlockModel {
}
