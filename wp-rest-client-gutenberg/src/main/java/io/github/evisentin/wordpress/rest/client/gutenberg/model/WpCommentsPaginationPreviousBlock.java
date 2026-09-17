package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/comments-pagination-previous}. The caller supplies compatible saved
 * HTML when changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination-previous /-->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpCommentsPaginationPreviousBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                                WpBlockSyntax syntax) implements WpSavedBlockModel {
}
