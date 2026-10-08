package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.AdapterSupport;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpHeadingBlock;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Typed heading text and level editing.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:heading {"level":3} -->
 * <h3 class="wp-block-heading">Introduction</h3>
 * <!-- /wp:heading -->
 * }</pre>
 */
public final class HeadingBlockAdapter implements WpBlockAdapter<WpHeadingBlock> {
    @Override
    public String blockName() {return "core/heading";}

    @Override
    public WpHeadingBlock fromBlock(WpBlock block) {
        AdapterSupport.check(block, blockName());
        Element heading = AdapterSupport.root(AdapterSupport.document(AdapterSupport.html(block)), "h1,h2,h3,h4,h5,h6");
        int level = Integer.parseInt(heading.tagName().substring(1));
        Map<String, Object> attributes = AdapterSupport.attributes(block.attributes());
        Object declared = attributes.remove("level");
        if (declared != null && (!(declared instanceof Number number) || number.doubleValue() != level)) {
            throw new IllegalArgumentException("Heading level does not match saved HTML");
        }
        return new WpHeadingBlock(heading.html(), level, attributes, block);
    }

    @Override
    public Class<WpHeadingBlock> modelType() {return WpHeadingBlock.class;}

    @Override
    public WpBlock toBlock(WpHeadingBlock model) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(model.contentHtml(), "contentHtml");
        if (model.level() < 1 || model.level() > 6) {
            throw new IllegalArgumentException("Heading level must be 1 through 6");
        }
        Document document;
        if (model.source() != null) {
            WpHeadingBlock original = fromBlock(model.source());
            AdapterSupport.unchangedAttributes(model.attributes(), original.attributes());
            if (model.contentHtml().equals(original.contentHtml()) && model.level() == original.level()) {
                return model.source();
            }
            document = AdapterSupport.document(AdapterSupport.html(model.source()));
        } else {
            AdapterSupport.newAttributes(model.attributes(), Set.of("className", "anchor", "style"));
            document = AdapterSupport.document("<h2 class=\"wp-block-heading\"></h2>");
            Element heading = document.body().child(0);
            AdapterSupport.common(heading, model.attributes());
            AdapterSupport.textAlign(heading, model.attributes());
        }
        Element heading = AdapterSupport.root(document, "h1,h2,h3,h4,h5,h6");
        heading.tagName("h" + model.level()).html(model.contentHtml());
        Map<String, Object> attributes = AdapterSupport.attributes(model.attributes());
        if (model.level() != 2) {
            attributes.put("level", model.level());
        }
        return AdapterSupport.block(blockName(), attributes, document.body().html());
    }
}
