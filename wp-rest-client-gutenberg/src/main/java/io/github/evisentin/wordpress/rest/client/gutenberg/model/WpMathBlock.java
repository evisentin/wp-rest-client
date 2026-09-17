package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/math}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:math {"latex":"x"} -->
 * <div class="wp-block-math"><math xmlns="http://www.w3.org/1998/Math/MathML"><mi>x</mi></math></div>
 * <!-- /wp:math -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpMathBlock(Map<String, Object> attributes, List<WpContentNode> content,
                          WpBlockSyntax syntax) implements WpSavedBlockModel {
}
