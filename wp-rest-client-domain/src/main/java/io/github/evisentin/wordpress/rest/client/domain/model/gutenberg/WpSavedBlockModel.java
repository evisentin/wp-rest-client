package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.List;
import java.util.Map;

/**
 * A named block's stored representation. Attributes and saved content must be edited together; implementing this
 * interface does not provide Gutenberg HTML rendering or schema validation.
 *
 * <p>Example saved audio content represented by a named model implementing this interface:</p>
 * <pre>{@code
 * <!-- wp:audio -->
 * <figure class="wp-block-audio"><audio controls src="https://example.com/audio.mp3"></audio></figure>
 * <!-- /wp:audio -->
 * }</pre>
 */
public interface WpSavedBlockModel {
    /**
     * @return block comment attributes
     */
    Map<String, Object> attributes();

    /**
     * @return ordered literal HTML fragments and child blocks
     */
    List<WpContentNode> content();

    /**
     * @return stored delimiter form
     */
    WpBlockSyntax syntax();
}
