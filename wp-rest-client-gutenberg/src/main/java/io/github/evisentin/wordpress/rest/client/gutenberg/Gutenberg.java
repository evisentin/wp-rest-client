package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.HeadingBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.ParagraphBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media.ImageBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpHeadingBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpImageBlock;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Fluent creation and editing of stored Gutenberg content. Instances are mutable and not thread-safe. HTML is trusted
 * input, not sanitized. Existing codec and adapter validation applies to every operation. Callbacks should modify only
 * the supplied editor, not the enclosing document.
 */
public final class Gutenberg {
    private static final DefaultWpGutenbergCodec CODEC = new DefaultWpGutenbergCodec();
    private static final ParagraphBlockAdapter PARAGRAPHS = new ParagraphBlockAdapter();
    private static final HeadingBlockAdapter HEADINGS = new HeadingBlockAdapter();
    private static final ImageBlockAdapter IMAGES = new ImageBlockAdapter();
    private List<WpContentNode> nodes;

    private Gutenberg(List<WpContentNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    /**
     * @return a shallow snapshot with an immutable top-level node list
     */
    public WpBlockDocument build() {return new WpBlockDocument(List.copyOf(nodes));}

    /**
     * Edits all heading blocks, including nested blocks, in document order. Source markup and unrelated nodes are
     * retained. If any edit fails, this document is unchanged.
     *
     * @param edit
     *         callback receiving the current block fields
     *
     * @return this document
     */
    public Gutenberg editHeadings(Consumer<Heading> edit) {
        Objects.requireNonNull(edit, "edit");
        return editBlocks(HEADINGS.blockName(), block -> {
            var editor = new Heading(HEADINGS.fromBlock(block));
            edit.accept(editor);
            return HEADINGS.toBlock(editor.model());
        });
    }

    /**
     * Edits all image blocks, including nested blocks, in document order. Source markup and unrelated nodes are
     * retained. If any edit fails, this document is unchanged.
     *
     * @param edit
     *         callback receiving the current block fields
     *
     * @return this document
     */
    public Gutenberg editImages(Consumer<Image> edit) {
        Objects.requireNonNull(edit, "edit");
        return editBlocks(IMAGES.blockName(), block -> {
            var editor = new Image(IMAGES.fromBlock(block));
            edit.accept(editor);
            return IMAGES.toBlock(editor.model());
        });
    }

    /**
     * Edits all paragraph blocks, including nested blocks, in document order. Source markup and unrelated nodes are
     * retained. If any edit fails, this document is unchanged.
     *
     * @param edit
     *         callback receiving the current block fields
     *
     * @return this document
     */
    public Gutenberg editParagraphs(Consumer<Paragraph> edit) {
        Objects.requireNonNull(edit, "edit");
        return editBlocks(PARAGRAPHS.blockName(), block -> {
            var editor = new Paragraph(PARAGRAPHS.fromBlock(block));
            edit.accept(editor);
            return PARAGRAPHS.toBlock(editor.model());
        });
    }

    /**
     * Appends a heading containing escaped plain text.
     *
     * @param text
     *         plain text
     *
     * @return this document
     */
    public Gutenberg heading(String text) {return heading(editor -> editor.text(text));}

    /**
     * Appends a new heading; configuration is applied immediately.
     *
     * @param configure
     *         block configuration
     *
     * @return this document
     */
    public Gutenberg heading(Consumer<Heading> configure) {
        var editor = new Heading();
        Objects.requireNonNull(configure, "configure").accept(editor);
        return node(HEADINGS.toBlock(editor.model()));
    }

    /**
     * Appends a new image; configuration is applied immediately.
     *
     * @param configure
     *         block configuration
     *
     * @return this document
     */
    public Gutenberg image(Consumer<Image> configure) {
        var editor = new Image();
        Objects.requireNonNull(configure, "configure").accept(editor);
        return node(IMAGES.toBlock(editor.model()));
    }

    /**
     * Appends an existing generic block or literal HTML fragment without adapting it.
     *
     * @param node
     *         content to append
     *
     * @return this document
     */
    public Gutenberg node(WpContentNode node) {
        nodes.add(Objects.requireNonNull(node, "node"));
        return this;
    }

    /**
     * Appends a paragraph containing escaped plain text.
     *
     * @param text
     *         plain text
     *
     * @return this document
     */
    public Gutenberg paragraph(String text) {return paragraph(editor -> editor.text(text));}

    /**
     * Appends a new paragraph; configuration is applied immediately.
     *
     * @param configure
     *         block configuration
     *
     * @return this document
     */
    public Gutenberg paragraph(Consumer<Paragraph> configure) {
        var editor = new Paragraph();
        Objects.requireNonNull(configure, "configure").accept(editor);
        return node(PARAGRAPHS.toBlock(editor.model()));
    }

    /**
     * @return stored markup without extra whitespace
     */
    public String serialize() {return serialize(false);}

    /**
     * @param prettyPrint
     *         whether to insert line breaks around block delimiters
     *
     * @return stored markup
     */
    public String serialize(boolean prettyPrint) {return CODEC.serialize(build(), prettyPrint);}

    // Explicit stack supports the same deeply nested documents as the codec.
    private Gutenberg editBlocks(String name, UnaryOperator<WpBlock> edit) {
        var stack = new ArrayDeque<Frame>();
        stack.push(new Frame(null, nodes));
        while (true) {
            Frame frame = stack.peek();
            if (frame.index < frame.input.size()) {
                WpContentNode node = frame.input.get(frame.index++);
                if (node instanceof WpBlock block) {
                    WpBlock updated = block.name().equals(name) ? edit.apply(block) : block;
                    stack.push(new Frame(updated, updated.content()));
                } else {
                    frame.output.add(node);
                }
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    nodes = frame.output;
                    return this;
                }
                WpBlock block = frame.block;
                boolean changed = false;
                for (int i = 0; i < frame.input.size(); i++) {
                    if (frame.input.get(i) != frame.output.get(i)) {
                        changed = true;
                        break;
                    }
                }
                stack.peek().output.add(changed
                        ? new WpBlock(block.name(), block.attributes(), List.copyOf(frame.output), block.syntax())
                        : block);
            }
        }
    }

    /**
     * @return an empty fluent document
     */
    public static Gutenberg document() {return new Gutenberg(List.of());}

    /**
     * @param rawContent
     *         stored content.raw markup
     *
     * @return a fluent document preserving the parsed node order
     */
    public static Gutenberg parse(String rawContent) {return new Gutenberg(CODEC.parse(rawContent).nodes());}

    private static String escape(String text) {
        return new Element("span").text(Objects.requireNonNull(text, "text")).html();
    }

    private static final class Frame {
        private final WpBlock block;
        private final List<WpContentNode> input;
        private final List<WpContentNode> output = new ArrayList<>();
        private int index;

        private Frame(WpBlock block, List<WpContentNode> input) {
            this.block = block;
            this.input = input;
        }
    }

    /**
     * Fluent paragraph fields; instances are supplied to document callbacks.
     */
    public static final class Paragraph {
        private final Map<String, Object> attributes = new LinkedHashMap<>();
        private String html = "";
        private WpBlock source;

        private Paragraph() {}

        private Paragraph(WpParagraphBlock model) {
            html = model.contentHtml();
            attributes.putAll(model.attributes());
            source = model.source();
        }

        /**
         * Sets HTML anchor ID for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         HTML anchor ID
         *
         * @return this editor
         */
        public Paragraph anchor(String value) {
            attributes.put("anchor", Objects.requireNonNull(value, "anchor"));
            return this;
        }

        /**
         * Sets CSS classes for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         CSS classes
         *
         * @return this editor
         */
        public Paragraph className(String value) {
            attributes.put("className", Objects.requireNonNull(value, "className"));
            return this;
        }

        /**
         * @return current inner rich-text HTML
         */
        public String html() {return html;}

        /**
         * @param value
         *         inner rich-text HTML
         *
         * @return this editor
         */
        public Paragraph html(String value) {
            html = value;
            return this;
        }

        /**
         * Replaces the inner HTML with escaped plain text.
         *
         * @param value
         *         plain text
         *
         * @return this editor
         */
        public Paragraph text(String value) {return html(escape(value));}

        private WpParagraphBlock model() {
            return new WpParagraphBlock(html, new LinkedHashMap<>(attributes), source);
        }
    }

    /**
     * Fluent heading fields; instances are supplied to document callbacks.
     */
    public static final class Heading {
        private final Map<String, Object> attributes = new LinkedHashMap<>();
        private String html = "";
        private int level = 2;
        private WpBlock source;

        private Heading() {}

        private Heading(WpHeadingBlock model) {
            html = model.contentHtml();
            level = model.level();
            attributes.putAll(model.attributes());
            source = model.source();
        }

        /**
         * Sets HTML anchor ID for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         HTML anchor ID
         *
         * @return this editor
         */
        public Heading anchor(String value) {
            attributes.put("anchor", Objects.requireNonNull(value, "anchor"));
            return this;
        }

        /**
         * Sets CSS classes for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         CSS classes
         *
         * @return this editor
         */
        public Heading className(String value) {
            attributes.put("className", Objects.requireNonNull(value, "className"));
            return this;
        }

        /**
         * @return current inner rich-text HTML
         */
        public String html() {return html;}

        /**
         * @param value
         *         inner rich-text HTML
         *
         * @return this editor
         */
        public Heading html(String value) {
            html = value;
            return this;
        }

        /**
         * @return current heading level from 1 through 6
         */
        public int level() {return level;}

        /**
         * @param value
         *         heading level from 1 through 6
         *
         * @return this editor
         */
        public Heading level(int value) {
            level = value;
            return this;
        }

        /**
         * Replaces the inner HTML with escaped plain text.
         *
         * @param value
         *         plain text
         *
         * @return this editor
         */
        public Heading text(String value) {return html(escape(value));}

        private WpHeadingBlock model() {
            return new WpHeadingBlock(html, level, new LinkedHashMap<>(attributes), source);
        }
    }

    /**
     * Fluent image fields; instances are supplied to document callbacks.
     */
    public static final class Image {
        private final Map<String, Object> attributes = new LinkedHashMap<>();
        private Long id = null;
        private String url = null;
        private String alt = "";
        private String captionHtml = null;
        private WpBlock source;

        private Image() {}

        private Image(WpImageBlock model) {
            id = model.mediaId();
            url = model.url();
            alt = model.altText();
            captionHtml = model.captionHtml();
            attributes.putAll(model.attributes());
            source = model.source();
        }

        /**
         * @return current alternative text
         */
        public String alt() {return alt;}

        /**
         * @param value
         *         alternative text
         *
         * @return this editor
         */
        public Image alt(String value) {
            alt = value;
            return this;
        }

        /**
         * Sets HTML anchor ID for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         HTML anchor ID
         *
         * @return this editor
         */
        public Image anchor(String value) {
            attributes.put("anchor", Objects.requireNonNull(value, "anchor"));
            return this;
        }

        /**
         * @return current optional rich-text caption; null removes it
         */
        public String captionHtml() {return captionHtml;}

        /**
         * @param value
         *         optional rich-text caption; null removes it
         *
         * @return this editor
         */
        public Image captionHtml(String value) {
            captionHtml = value;
            return this;
        }

        /**
         * Sets CSS classes for a new block. Existing block attributes must remain unchanged.
         *
         * @param value
         *         CSS classes
         *
         * @return this editor
         */
        public Image className(String value) {
            attributes.put("className", Objects.requireNonNull(value, "className"));
            return this;
        }

        /**
         * @return current optional positive WordPress media ID
         */
        public Long id() {return id;}

        /**
         * @param value
         *         optional positive WordPress media ID
         *
         * @return this editor
         */
        public Image id(Long value) {
            id = value;
            return this;
        }

        /**
         * @param value
         *         positive WordPress media ID
         *
         * @return this editor
         */
        public Image id(long value) {return id(Long.valueOf(value));}

        /**
         * @return current image URL
         */
        public String url() {return url;}

        /**
         * @param value
         *         image URL
         *
         * @return this editor
         */
        public Image url(String value) {
            url = value;
            return this;
        }

        private WpImageBlock model() {
            return new WpImageBlock(id, url, alt, captionHtml, new LinkedHashMap<>(attributes), source);
        }
    }
}
