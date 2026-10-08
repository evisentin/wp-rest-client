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

    /**
     * Creates a shallow, mutable copy of comment attributes.
     *
     * @param attributes
     *         non-null source map; null values are retained
     *
     * @return copied attributes
     */
    public static Map<String, Object> attributes(Map<String, Object> attributes) {
        return new LinkedHashMap<>(Objects.requireNonNull(attributes, "attributes"));
    }

    /**
     * Creates a paired block with one literal HTML fragment.
     *
     * @param name
     *         fully qualified block name
     * @param attributes
     *         comment attributes to copy
     * @param html
     *         saved HTML
     *
     * @return generic paired block
     */
    public static WpBlock block(String name, Map<String, Object> attributes, String html) {
        return new WpBlock(name, attributes(attributes), List.of(new WpHtmlFragment(html)), WpBlockSyntax.PAIRED);
    }

    /**
     * Checks the block name and requires paired syntax.
     *
     * @param block
     *         block to inspect
     * @param name
     *         expected fully qualified name
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
    public static void check(WpBlock block, String name) {
        Objects.requireNonNull(block, "block");
        if (!name.equals(block.name()) || block.syntax() != WpBlockSyntax.PAIRED) {
            throw new IllegalArgumentException("Expected paired " + name + " block");
        }
    }

    /**
     * Applies className and anchor options to an HTML element.
     *
     * @param element
     *         element to modify
     * @param attributes
     *         comment attributes
     */
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

    /**
     * Parses trusted saved HTML as a body fragment with pretty printing disabled.
     *
     * @param html
     *         saved HTML; no sanitization is performed
     *
     * @return parsed document
     */
    public static Document document(String html) {
        Document document = Jsoup.parseBodyFragment(html);
        document.outputSettings().prettyPrint(false);
        return document;
    }

    /**
     * Concatenates leaf HTML fragments, rejecting nested blocks.
     *
     * @param block
     *         block containing only HTML fragments
     *
     * @return saved HTML
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
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

    /**
     * Rejects options unsupported by a new-block generator.
     *
     * @param attributes
     *         supplied options
     * @param supported
     *         allowed option names
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
    public static void newAttributes(Map<String, Object> attributes, Set<String> supported) {
        for (String key : attributes.keySet()) {
            if (!supported.contains(key)) {
                throw new IllegalArgumentException("Unsupported option for new block: " + key);
            }
        }
    }

    /**
     * Requires one matching top-level body element.
     *
     * @param document
     *         parsed saved HTML
     * @param selector
     *         CSS selector for the required element
     *
     * @return matching root element
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
    public static Element root(Document document, String selector) {
        if (document.body().childrenSize() != 1 || !document.body().child(0).is(selector)) {
            throw new IllegalArgumentException("Expected one " + selector + " root element");
        }
        return document.body().child(0);
    }

    /**
     * Applies supported text alignment as a CSS class. Only style.typography.textAlign is accepted.
     *
     * @param element
     *         element to modify
     * @param attributes
     *         comment attributes
     *
     * @return left, center or right, or null when style is absent
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
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

    /**
     * Rejects changes to comment options that require regenerating saved markup.
     *
     * @param current
     *         edited options
     * @param original
     *         original options
     *
     * @throws IllegalArgumentException
     *         if the supplied content or options are unsupported
     */
    public static void unchangedAttributes(Map<String, Object> current, Map<String, Object> original) {
        if (!current.equals(original)) {
            throw new IllegalArgumentException("Changing saved block options requires regenerating their markup; "
                                               + "edit the generic WpBlock or create a new supported block instead");
        }
    }
}
