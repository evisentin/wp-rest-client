package io.github.evisentin.wordpress.rest.client.gutenberg.model;

/**
 * A block or a literal HTML fragment within Gutenberg content.
 *
 * <p>Example content sequence: HTML fragment, block, HTML fragment:</p>
 * <pre>{@code
 * <div class="content">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * }</pre>
 */
public sealed interface WpContentNode permits WpBlock, WpHtmlFragment {
}
