package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;

/**
 * Parses stored Gutenberg markup from {@code content.raw}, not rendered HTML.
 *
 * <p>Example raw input parsed into a document with one paragraph block:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 */
public interface WpBlockParser {
    /**
     * Parses raw content, preserving ordinary HTML and unknown block types.
     *
     * @param rawContent
     *         non-null raw post or page content
     *
     * @return parsed document
     *
     * @throws WpBlockParseException
     *         when block syntax is malformed
     */
    WpBlockDocument parse(String rawContent) throws WpBlockParseException;
}
