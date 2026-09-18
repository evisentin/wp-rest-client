package io.github.evisentin.wordpress.rest.client.gutenberg.model.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.reusable.WpSavedBlockModel;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Saved-content model for WordPress 7.1 {@code core/embed}. The caller supplies compatible saved HTML when changing
 * markup-related attributes.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:embed {"url":"https://www.youtube.com/watch?v=example","type":"video","providerNameSlug":"youtube"} -->
 * <figure class="wp-block-embed is-type-video is-provider-youtube wp-block-embed-youtube"><div class="wp-block-embed__wrapper">
 * https://www.youtube.com/watch?v=example
 * </div></figure>
 * <!-- /wp:embed -->
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
public record WpEmbedBlock(
        @Singular("attribute") Map<String, Object> attributes,
        @Singular("contentNode") List<WpContentNode> content,
        WpBlockSyntax syntax) implements WpSavedBlockModel {
}
