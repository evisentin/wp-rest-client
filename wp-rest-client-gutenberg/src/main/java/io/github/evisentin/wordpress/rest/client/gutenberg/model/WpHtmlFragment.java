package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import lombok.Builder;

/**
 * Literal HTML preserved without parsing or reformatting.
 *
 * <p>Example literal HTML fragment within a paragraph block (without its block delimiters):</p>
 * <pre>{@code
 * <p>Hello <strong>world</strong>.</p>
 * }</pre>
 *
 * @param html
 *         HTML markup, text, comments or whitespace
 */
@Builder
public record WpHtmlFragment(String html) implements WpContentNode {
}
