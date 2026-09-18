package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * Complete raw post or page content in document order. The supplied list is retained without copying; callers are
 * responsible for its validity and mutation lifecycle.
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
@Builder
public record WpBlockDocument(@Singular("node") List<WpContentNode> nodes) {
}
