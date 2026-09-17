package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base adapter preserving stored attributes, HTML, nested blocks and delimiter form.
 *
 * <p>For example, {@link AudioBlockAdapter} preserves this saved audio block:</p>
 * <pre>{@code
 * <!-- wp:audio -->
 * <figure class="wp-block-audio"><audio controls src="https://example.com/audio.mp3"></audio></figure>
 * <!-- /wp:audio -->
 * }</pre>
 */
public abstract class SavedBlockAdapter<T extends WpSavedBlockModel> implements WpBlockAdapter<T> {
    private final String name;
    private final Class<T> type;
    private final Factory<T> factory;

    /**
     * Creates an adapter using the named model's constructor.
     */
    protected SavedBlockAdapter(String name, Class<T> type, Factory<T> factory) {
        this.name = name;
        this.type = type;
        this.factory = factory;
    }

    @Override
    public final String blockName() {return name;}

    @Override
    public final T fromBlock(WpBlock block) {
        Objects.requireNonNull(block, "block");
        if (!name.equals(block.name())) {
            throw new IllegalArgumentException("Expected " + name + ", got " + block.name());
        }
        validate(block.content(), block.syntax());
        return factory.create(AdapterSupport.attributes(block.attributes()), List.copyOf(block.content()), block.syntax());
    }

    @Override
    public final Class<T> modelType() {return type;}

    @Override
    public final WpBlock toBlock(T model) {
        Objects.requireNonNull(model, "model");
        validate(model.content(), model.syntax());
        return new WpBlock(name, AdapterSupport.attributes(model.attributes()), List.copyOf(model.content()), model.syntax());
    }

    private static void validate(List<WpContentNode> content, WpBlockSyntax syntax) {
        Objects.requireNonNull(content, "content");
        Objects.requireNonNull(syntax, "syntax");
        if (syntax == WpBlockSyntax.SELF_CLOSING && !content.isEmpty()) {
            throw new IllegalArgumentException("Self-closing blocks cannot contain saved content");
        }
    }

    /**
     * Constructor function for a named saved-content model.
     */
    @FunctionalInterface
    protected interface Factory<T> {
        /**
         * Creates a model from its stored representation.
         */
        T create(Map<String, Object> attributes, List<WpContentNode> content, WpBlockSyntax syntax);
    }
}
