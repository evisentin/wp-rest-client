package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

import java.util.List;

/**
 * Complete raw post or page content in document order.
 *
 * <p>Example document containing a heading followed by a paragraph:</p>
 * <pre>{@code
 * <!-- wp:heading -->
 * <h2 class="wp-block-heading">Introduction</h2>
 * <!-- /wp:heading -->
 *
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 *
 * @param nodes
 *         blocks and HTML outside block delimiters, including whitespace
 */
public record WpBlockDocument(List<WpContentNode> nodes) {
}
