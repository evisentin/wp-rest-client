package io.github.evisentin.wordpress.rest.client.gutenberg.model.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;

import java.util.Map;

/**
 * Typed editing model for image blocks. Conversion to saved markup requires a compatible {@link WpBlockAdapter}.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:image {"id":42} -->
 * <figure class="wp-block-image"><img src="https://example.com/photo.jpg" alt="A mountain" class="wp-image-42"/></figure>
 * <!-- /wp:image -->
 * }</pre>
 *
 * @param mediaId
 *         optional WordPress media ID
 * @param url
 *         image source URL
 * @param altText
 *         alternative text
 * @param captionHtml
 *         optional rich-text caption
 * @param attributes
 *         remaining comment attributes; media ID is represented by the dedicated field
 * @param source
 *         original block for preserving saved markup, or null for a new block
 */
public record WpImageBlock(Long mediaId, String url, String altText, String captionHtml, Map<String, Object> attributes,
                           WpBlock source) {
    /**
     * Creates a new block without previously saved markup.
     */
    public WpImageBlock(Long mediaId, String url, String altText, String captionHtml, Map<String, Object> attributes) {
        this(mediaId, url, altText, captionHtml, attributes, null);
    }
}
