package io.github.evisentin.wordpress.rest.client.gutenberg.model.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/verse}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:verse -->
 * <pre class="wp-block-verse">The morning sun
 * Across the sea</pre>
 * <!-- /wp:verse -->
 * }</pre>
 *
 * @param attributes
 *         block comment attributes, including unknown options
 * @param content
 *         ordered saved HTML fragments and nested blocks
 * @param syntax
 *         original paired or self-closing delimiter form
 */
@Builder
public record WpVerseBlock(
        @Singular("attribute") Map<String, Object> attributes,
        @Singular("contentNode") List<WpContentNode> content,
        WpBlockSyntax syntax) implements WpSavedBlockModel {
}
