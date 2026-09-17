package io.github.evisentin.wordpress.rest.client.gutenberg.model;

import java.util.List;
import java.util.Map;

/**
 * Generic representation of a core or plugin block.
 *
 * <p>Example of a block represented by this generic model:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 *
 * @param name
 *         fully qualified block name, for example {@code core/paragraph}
 * @param attributes
 *         comment attributes containing JSON-compatible values, including null
 * @param content
 *         ordered HTML fragments and nested blocks, preserving child insertion positions
 * @param syntax
 *         delimiter form; self-closing blocks must have empty content
 */
public record WpBlock(String name, Map<String, Object> attributes,
                      List<WpContentNode> content, WpBlockSyntax syntax) implements WpContentNode {
}
