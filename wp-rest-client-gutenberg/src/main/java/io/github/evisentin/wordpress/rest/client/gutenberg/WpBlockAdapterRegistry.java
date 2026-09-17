package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;

import java.util.Optional;

/**
 * Lookup contract for optional typed block adapters; unsupported blocks remain generic.
 *
 * <p>Example block whose name can be used to look up a paragraph adapter:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 * <p>Use {@code core/paragraph} for the block-name lookup.</p>
 */
public interface WpBlockAdapterRegistry {
    /**
     * @param blockName
     *         fully qualified block name
     *
     * @return adapter if supported
     */
    Optional<WpBlockAdapter<?>> findByBlockName(String blockName);

    /**
     * @param modelType
     *         Java model type
     * @param <T>
     *         typed block model
     *
     * @return adapter if supported
     */
    <T> Optional<WpBlockAdapter<T>> findByModelType(Class<T> modelType);
}
