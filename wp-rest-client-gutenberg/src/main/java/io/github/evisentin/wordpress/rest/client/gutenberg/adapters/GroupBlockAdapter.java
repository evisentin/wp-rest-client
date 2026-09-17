package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpGroupBlock;

import java.util.List;
import java.util.Objects;

/**
 * Adapts a group's saved wrapper fragments and nested blocks without regenerating layout HTML.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:group -->
 * <div class="wp-block-group">
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * </div>
 * <!-- /wp:group -->
 * }</pre>
 * <p>The adapter preserves supplied content; it does not generate this markup or render the block.</p>
 */
public final class GroupBlockAdapter implements WpBlockAdapter<WpGroupBlock> {
    @Override
    public String blockName() {return "core/group";}

    @Override
    public WpGroupBlock fromBlock(WpBlock block) {
        AdapterSupport.check(block, blockName());
        return new WpGroupBlock(List.copyOf(block.content()), AdapterSupport.attributes(block.attributes()));
    }

    @Override
    public Class<WpGroupBlock> modelType() {return WpGroupBlock.class;}

    @Override
    public WpBlock toBlock(WpGroupBlock model) {
        Objects.requireNonNull(model, "model");
        return new WpBlock(blockName(), AdapterSupport.attributes(model.attributes()),
                List.copyOf(model.content()), WpBlockSyntax.PAIRED);
    }
}
