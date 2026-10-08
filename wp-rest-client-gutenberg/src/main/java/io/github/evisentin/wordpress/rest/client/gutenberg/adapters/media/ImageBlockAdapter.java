package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.AdapterSupport;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpImageBlock;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Edits image fields while retaining an existing figure, link and unrecognized markup.
 *
 * <p>Example stored block representation (illustrative saved markup):</p>
 * <pre>{@code
 * <!-- wp:image {"id":42} -->
 * <figure class="wp-block-image"><img src="https://example.com/photo.jpg" alt="A mountain" class="wp-image-42"/></figure>
 * <!-- /wp:image -->
 * }</pre>
 */
public final class ImageBlockAdapter implements WpBlockAdapter<WpImageBlock> {
    @Override
    public String blockName() {return "core/image";}

    @Override
    public WpImageBlock fromBlock(WpBlock block) {
        AdapterSupport.check(block, blockName());
        Document document = AdapterSupport.document(AdapterSupport.html(block));
        Element image = image(document);
        Element caption = document.selectFirst("figcaption");
        Map<String, Object> attributes = AdapterSupport.attributes(block.attributes());
        Object id = attributes.remove("id");
        Long mediaId = null;
        if (id != null) {
            if (!(id instanceof Number number) || number.doubleValue() != number.longValue() || number.longValue() < 1) {
                throw new IllegalArgumentException("Image ID must be a positive integer");
            }
            mediaId = number.longValue();
        }
        return new WpImageBlock(mediaId, image.attr("src"), image.attr("alt"),
                caption == null ? null : caption.html(), attributes, block);
    }

    @Override
    public Class<WpImageBlock> modelType() {return WpImageBlock.class;}

    @Override
    public WpBlock toBlock(WpImageBlock model) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(model.url(), "url");
        if (model.mediaId() != null && model.mediaId() < 1) {
            throw new IllegalArgumentException("Image ID must be positive");
        }
        Document document;
        WpImageBlock original = null;
        if (model.source() != null) {
            original = fromBlock(model.source());
            AdapterSupport.unchangedAttributes(model.attributes(), original.attributes());
            if (model.equals(original)) {
                return model.source();
            }
            document = AdapterSupport.document(AdapterSupport.html(model.source()));
        } else {
            AdapterSupport.newAttributes(model.attributes(), Set.of("className", "anchor", "sizeSlug", "linkDestination"));
            if (model.attributes().get("linkDestination") != null
                && !"none".equals(model.attributes().get("linkDestination"))) {
                throw new IllegalArgumentException("New linked images require explicit saved markup");
            }
            document = AdapterSupport.document("<figure class=\"wp-block-image\"><img src=\"\" alt=\"\"></figure>");
            Element figure = document.body().child(0);
            AdapterSupport.common(figure, model.attributes());
            if (model.attributes().get("sizeSlug") != null) {
                String size = model.attributes().get("sizeSlug").toString();
                if (!size.matches("[a-zA-Z0-9_-]+")) {
                    throw new IllegalArgumentException("Invalid image size slug");
                }
                figure.addClass("size-" + size);
            }
        }
        Element image = image(document);
        image.attr("src", model.url()).attr("alt", Objects.requireNonNullElse(model.altText(), ""));
        if (original == null || !Objects.equals(original.mediaId(), model.mediaId())) {
            Set<String> classes = new java.util.LinkedHashSet<>(image.classNames());
            classes.removeIf(name -> name.matches("wp-image-\\d+"));
            if (model.mediaId() != null) {
                classes.add("wp-image-" + model.mediaId());
            }
            image.classNames(classes);
            if (classes.isEmpty()) {
                image.removeAttr("class");
            }
        }
        if (original != null && !original.url().equals(model.url())) {
            image.removeAttr("srcset").removeAttr("sizes");
            if ("media".equals(model.attributes().get("linkDestination")) && image.parent().is("a")) {
                image.parent().attr("href", model.url());
            }
        }
        Element caption = document.selectFirst("figcaption");
        if (model.captionHtml() == null || model.captionHtml().isEmpty()) {
            if (caption != null) {
                caption.remove();
            }
        } else {
            if (caption == null) {
                Element figure = document.selectFirst("figure");
                if (figure == null) {
                    throw new IllegalArgumentException("Adding a caption requires a figure wrapper");
                }
                caption = figure.appendElement("figcaption").addClass("wp-element-caption");
            }
            caption.html(model.captionHtml());
        }
        Map<String, Object> attributes = AdapterSupport.attributes(model.attributes());
        if (model.mediaId() != null) {
            attributes.put("id", model.mediaId());
        }
        return AdapterSupport.block(blockName(), attributes, document.body().html());
    }

    private static Element image(Document document) {
        if (document.select("img").size() != 1) {
            throw new IllegalArgumentException("Expected exactly one image");
        }
        return document.selectFirst("img");
    }
}
