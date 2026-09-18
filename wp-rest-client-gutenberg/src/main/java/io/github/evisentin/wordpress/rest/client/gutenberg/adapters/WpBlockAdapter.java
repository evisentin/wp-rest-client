package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;

/**
 * Converts a supported block to and from a typed model. Saved-content adapters preserve caller-supplied HTML;
 * specialized editing adapters can regenerate supported markup. Conversion does not render dynamic blocks or validate
 * Gutenberg editor save output.
 *
 * <p>Example block handled by a paragraph adapter implementing this contract:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 *
 * @param <T>
 *         typed block model
 */
public interface WpBlockAdapter<T> {
    /**
     * @return fully qualified supported block name
     */
    String blockName();

    /**
     * @param block
     *         supported generic block
     *
     * @return typed editing model
     */
    T fromBlock(WpBlock block);

    /**
     * @return supported Java model type
     */
    Class<T> modelType();

    /**
     * @param model
     *         typed editing model
     *
     * @return block with compatible saved markup
     */
    WpBlock toBlock(T model);
}
