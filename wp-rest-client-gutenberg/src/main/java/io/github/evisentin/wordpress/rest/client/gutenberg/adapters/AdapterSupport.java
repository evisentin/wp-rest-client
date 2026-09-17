package io.github.evisentin.wordpress.rest.client.gutenberg.adapters;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * Shared saved-markup handling; deliberately does not emulate all Gutenberg block supports.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdapterSupport {

    public static Map<String, Object> attributes(Map<String, Object> attributes) {
        return new LinkedHashMap<>(Objects.requireNonNull(attributes, "attributes"));
    }

    public static WpBlock block(String name, Map<String, Object> attributes, String html) {
        return new WpBlock(name, attributes(attributes), List.of(new WpHtmlFragment(html)), WpBlockSyntax.PAIRED);
    }

    public static void check(WpBlock block, String name) {
        Objects.requireNonNull(block, "block");
        if (!name.equals(block.name()) || block.syntax() != WpBlockSyntax.PAIRED) {
            throw new IllegalArgumentException("Expected paired " + name + " block");
        }
    }

    public static void common(Element element, Map<String, Object> attributes) {
        Object className = attributes.get("className");
        if (className != null && !className.toString().isBlank()) {
            for (String name : className.toString().split("\\s+")) {
                element.addClass(name);
            }
        }
        if (attributes.get("anchor") != null) {
            element.attr("id", attributes.get("anchor").toString());
        }
    }

    public static Document document(String html) {
        Document document = Jsoup.parseBodyFragment(html);
        document.outputSettings().prettyPrint(false);
        return document;
    }

    public static String html(WpBlock block) {
        StringBuilder html = new StringBuilder();
        for (WpContentNode node : block.content()) {
            if (!(node instanceof WpHtmlFragment(String html1))) {
                throw new IllegalArgumentException("Expected leaf HTML content in " + block.name());
            }
            html.append(html1);
        }
        return html.toString();
    }

    public static void newAttributes(Map<String, Object> attributes, Set<String> supported) {
        for (String key : attributes.keySet()) {
            if (!supported.contains(key)) {
                throw new IllegalArgumentException("Unsupported option for new block: " + key);
            }
        }
    }

    public static Element root(Document document, String selector) {
        if (document.body().childrenSize() != 1 || !document.body().child(0).is(selector)) {
            throw new IllegalArgumentException("Expected one " + selector + " root element");
        }
        return document.body().child(0);
    }

    public static String textAlign(Element element, Map<String, Object> attributes) {
        Object style = attributes.get("style");
        if (style == null) {
            return null;
        }
        if (!(style instanceof Map<?, ?> styles) || !styles.keySet().equals(Set.of("typography"))
            || !(styles.get("typography") instanceof Map<?, ?> typography)
            || !typography.keySet().equals(Set.of("textAlign"))) {
            throw new IllegalArgumentException("New blocks support only style.typography.textAlign");
        }
        Object align = typography.get("textAlign");
        if (!(align instanceof String value) || !Set.of("left", "center", "right").contains(value)) {
            throw new IllegalArgumentException("Invalid text alignment");
        }
        element.addClass("has-text-align-" + value);
        return value;
    }

    public static void unchangedAttributes(Map<String, Object> current, Map<String, Object> original) {
        if (!current.equals(original)) {
            throw new IllegalArgumentException("Changing saved block options requires regenerating their markup; "
                                               + "edit the generic WpBlock or create a new supported block instead");
        }
    }
}
