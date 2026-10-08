package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/missing}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:missing {"originalName":"vendor/example"} -->
 * <div>Content from an unavailable plugin.</div>
 * <!-- /wp:missing -->
 * }</pre>
 * <p>This example uses the explicit block form represented by this model.
 * WordPress may store this content without block delimiters, for example
 * {@code <div>Content from an unavailable plugin.</div>}; the codec preserves that form as literal HTML.</p>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
@Builder
public record WpMissingBlock(
        @Singular("attribute") Map<String, Object> attributes,
        @Singular("contentNode") List<WpContentNode> content,
        WpBlockSyntax syntax) implements WpSavedBlockModel {
}
