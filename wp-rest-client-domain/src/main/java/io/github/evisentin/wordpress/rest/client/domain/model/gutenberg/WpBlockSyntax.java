package io.github.evisentin.wordpress.rest.client.domain.model.gutenberg;

/**
 * The stored delimiter form, independent of whether a block renders dynamically.
 *
 * <p>Examples of paired and self-closing block delimiters:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 *
 * <!-- wp:latest-posts {"postsToShow":3} /-->
 * }</pre>
 */
public enum WpBlockSyntax {
    /**
     * Opening and closing block comments surrounding content.
     */
    PAIRED,
    /**
     * A single self-closing block comment.
     */
    SELF_CLOSING
}
