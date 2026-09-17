package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/terms-query}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:terms-query -->
 * <!-- wp:term-template -->
 * <!-- wp:term-name {"level":2,"isLink":true} /-->
 * <!-- /wp:term-template -->
 * <!-- /wp:terms-query -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpTermsQueryBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                WpBlockSyntax syntax) implements WpSavedBlockModel {
}
