package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/accordion-item}. The caller supplies compatible saved HTML when
 * changing markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:accordion-item -->
 * <div class="wp-block-accordion-item">
 * <!-- wp:accordion-heading -->
 * <h3 class="wp-block-accordion-heading"><button class="wp-block-accordion-heading__toggle"><span class="wp-block-accordion-heading__toggle-title">Question</span><span class="wp-block-accordion-heading__toggle-icon" aria-hidden="true">+</span></button></h3>
 * <!-- /wp:accordion-heading -->
 * <!-- wp:accordion-panel -->
 * <div role="region" class="wp-block-accordion-panel">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:accordion-panel -->
 * </div>
 * <!-- /wp:accordion-item -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
public record WpAccordionItemBlock(Map<String, Object> attributes, List<WpContentNode> content,
                                   WpBlockSyntax syntax) implements WpSavedBlockModel {
}
