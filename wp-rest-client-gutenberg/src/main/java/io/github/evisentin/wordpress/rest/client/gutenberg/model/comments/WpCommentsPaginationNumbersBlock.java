package io.github.evisentin.wordpress.rest.client.gutenberg.model.comments;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/comments-pagination-numbers}. The caller supplies compatible saved
 * HTML when changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:comments-pagination-numbers /-->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpCommentsPaginationNumbersBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                               WpBlockSyntax syntax) implements WpSavedBlockModel {
}
