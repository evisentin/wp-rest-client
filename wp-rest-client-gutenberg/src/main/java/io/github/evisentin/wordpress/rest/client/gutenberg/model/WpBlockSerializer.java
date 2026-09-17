package io.github.evisentin.wordpress.rest.client.gutenberg.model;

/**
 * Serializes a document to the raw content string accepted by post and page requests.
 *
 * <p>Example serialized output for a document with one paragraph block:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 */
public interface WpBlockSerializer {
    /**
     * Serializes block delimiters, escaped JSON attributes and ordered content. HTML fragments are preserved; exact
     * original comment formatting is not guaranteed.
     *
     * @param document
     *         document to serialize
     *
     * @return Gutenberg markup, not server-rendered HTML
     */
    String serialize(WpBlockDocument document);
}
