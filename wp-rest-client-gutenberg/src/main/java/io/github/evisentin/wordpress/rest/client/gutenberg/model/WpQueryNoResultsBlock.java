package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/query-no-results}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:query-no-results -->
 * <!-- wp:paragraph -->
 * <p>No posts found.</p>
 * <!-- /wp:paragraph -->
 * <!-- /wp:query-no-results -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpQueryNoResultsBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                    WpBlockSyntax syntax) implements WpSavedBlockModel {
}
