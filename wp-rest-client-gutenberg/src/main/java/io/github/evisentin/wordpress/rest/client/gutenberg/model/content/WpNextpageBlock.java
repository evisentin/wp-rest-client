package io.github.evisentin.wordpress.rest.client.gutenberg.model.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/nextpage}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:nextpage /-->
 * }</pre>
 * <p>This example uses the explicit block form represented by this model.
 * WordPress may store this content without block delimiters, for example {@code <!--nextpage-->}; the codec preserves
 * that form as literal HTML.</p>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
@Builder
public record WpNextpageBlock(
        @Singular("attribute") Map<String, Object> attributes,
        @Singular("contentNode") List<WpContentNode> content,
        WpBlockSyntax syntax) implements WpSavedBlockModel {
}
