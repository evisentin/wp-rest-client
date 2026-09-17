package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.internal.GutenbergParser;
import io.github.evisentin.wordpress.rest.client.gutenberg.internal.GutenbergSerializer;

/**
 * Parses and serializes raw Gutenberg content without rendering HTML or running block save functions. Instances can be
 * shared between threads. Malformed block syntax is rejected rather than repaired.
 */
public final class DefaultWpGutenbergCodec implements WpGutenbergCodec {
    private final GutenbergParser parser = new GutenbergParser();
    private final GutenbergSerializer serializer = new GutenbergSerializer();

    @Override
    public WpBlockDocument parse(String rawContent) {
        return parser.parse(rawContent);
    }

    @Override
    public String serialize(WpBlockDocument document) {
        return serializer.serialize(document);
    }
}
