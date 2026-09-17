package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpBlock;
import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.WpParagraphBlock;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Set;

/**
 * Typed paragraph content editing with preservation of existing wrapper markup.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello <strong>world</strong>.</p>
 * <!-- /wp:paragraph -->
 * }</pre>
 */
public final class ParagraphBlockAdapter implements WpBlockAdapter<WpParagraphBlock> {
    @Override
    public String blockName() {return "core/paragraph";}

    @Override
    public WpParagraphBlock fromBlock(WpBlock block) {
        AdapterSupport.check(block, blockName());
        Element paragraph = AdapterSupport.root(AdapterSupport.document(AdapterSupport.html(block)), "p");
        return new WpParagraphBlock(paragraph.html(), AdapterSupport.attributes(block.attributes()), block);
    }

    @Override
    public Class<WpParagraphBlock> modelType() {return WpParagraphBlock.class;}

    @Override
    public WpBlock toBlock(WpParagraphBlock model) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(model.contentHtml(), "contentHtml");
        Document document;
        if (model.source() != null) {
            WpParagraphBlock original = fromBlock(model.source());
            AdapterSupport.unchangedAttributes(model.attributes(), original.attributes());
            if (model.contentHtml().equals(original.contentHtml())) {
                return model.source();
            }
            document = AdapterSupport.document(AdapterSupport.html(model.source()));
        } else {
            AdapterSupport.newAttributes(model.attributes(), Set.of("className", "anchor", "style", "dropCap", "direction"));
            document = AdapterSupport.document("<p></p>");
            Element paragraph = document.body().child(0);
            AdapterSupport.common(paragraph, model.attributes());
            String align = AdapterSupport.textAlign(paragraph, model.attributes());
            if (model.attributes().get("direction") != null) {
                String direction = model.attributes().get("direction").toString();
                if (!Set.of("ltr", "rtl").contains(direction)) {
                    throw new IllegalArgumentException("Invalid paragraph direction");
                }
                paragraph.attr("dir", direction);
            }
            if (model.attributes().get("dropCap") != null && !(model.attributes().get("dropCap") instanceof Boolean)) {
                throw new IllegalArgumentException("dropCap must be a boolean");
            }
            if (Boolean.TRUE.equals(model.attributes().get("dropCap"))) {
                if ("left".equals(align) || "right".equals(align)) {
                    throw new IllegalArgumentException("Aligned drop caps depend on the editor locale; supply saved markup");
                }
                if (!"center".equals(align)) {
                    paragraph.addClass("has-drop-cap");
                }
            }
        }
        AdapterSupport.root(document, "p").html(model.contentHtml());
        return AdapterSupport.block(blockName(), model.attributes(), document.body().html());
    }
}
