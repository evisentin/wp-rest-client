package io.github.evisentin.wordpress.rest.client.gutenberg.model.layout;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/separator}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:separator -->
 * <hr class="wp-block-separator has-alpha-channel-opacity"/>
 * <!-- /wp:separator -->
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
public record WpSeparatorBlock(
        @Singular("attribute") Map<String, Object> attributes,
        @Singular("contentNode") List<WpContentNode> content,
        WpBlockSyntax syntax) implements WpSavedBlockModel {
}
