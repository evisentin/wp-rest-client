package io.github.evisentin.wordpress.rest.client.gutenberg.model.layout;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/text-columns}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:text-columns {"columns":2} -->
 * <div class="wp-block-text-columns alignnone columns-2"><div class="wp-block-column"><p>Left</p></div><div class="wp-block-column"><p>Right</p></div></div>
 * <!-- /wp:text-columns -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpTextColumnsBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                 WpBlockSyntax syntax) implements WpSavedBlockModel {
}
