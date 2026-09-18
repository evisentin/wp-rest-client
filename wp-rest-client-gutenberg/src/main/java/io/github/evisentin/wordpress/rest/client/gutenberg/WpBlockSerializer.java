package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;

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
     * <p>
     * The content tree must be acyclic and must not be mutated during serialization. Attribute values must be
     * JSON-compatible. Changing attributes does not regenerate saved HTML.
     *
     * @param document
     *         non-null document to serialize
     *
     * @return Gutenberg markup, not server-rendered HTML
     *
     * @throws IllegalArgumentException
     *         if a block name or structure is invalid, or attributes cannot be serialized as JSON
     * @throws NullPointerException
     *         if the document or a content node is null
     */
    String serialize(WpBlockDocument document);
}
