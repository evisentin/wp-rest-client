package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/accordion-heading}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:accordion-heading -->
 * <h3 class="wp-block-accordion-heading"><button class="wp-block-accordion-heading__toggle"><span class="wp-block-accordion-heading__toggle-title">Question</span><span class="wp-block-accordion-heading__toggle-icon" aria-hidden="true">+</span></button></h3>
 * <!-- /wp:accordion-heading -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpAccordionHeadingBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                      WpBlockSyntax syntax) implements WpSavedBlockModel {
}
