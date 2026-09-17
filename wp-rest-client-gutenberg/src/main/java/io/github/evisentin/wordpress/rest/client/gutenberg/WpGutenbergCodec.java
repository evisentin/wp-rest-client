package io.github.evisentin.wordpress.rest.client.gutenberg;

/**
 * Combined parsing and serialization contract. Implementations supply Gutenberg syntax handling; this domain API
 * supplies no parser implementation.
 *
 * <p>Example stored block accepted by the parser and emitted by the serializer:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 */
public interface WpGutenbergCodec extends WpBlockParser, WpBlockSerializer {
}
