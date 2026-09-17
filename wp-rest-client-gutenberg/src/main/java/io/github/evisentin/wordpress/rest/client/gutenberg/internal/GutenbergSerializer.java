package io.github.evisentin.wordpress.rest.client.gutenberg.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.domain.model.gutenberg.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Internal serializer for stored block markup; does not execute Gutenberg save functions.
 */
public final class GutenbergSerializer implements WpBlockSerializer {
    private static final Pattern NAME = Pattern.compile("[a-z][a-z0-9_-]*/[a-z][a-z0-9_-]*");
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize(WpBlockDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder output = new StringBuilder();
        Deque<Frame> pending = new ArrayDeque<>();
        pending.push(new Frame(Objects.requireNonNull(document.nodes(), "nodes").iterator(), null));
        var nameMatcher = NAME.matcher("");
        while (!pending.isEmpty()) {
            Frame frame = pending.peek();
            if (!frame.nodes().hasNext()) {
                pending.pop();
                if (frame.blockName() != null) {
                    output.append("<!-- /wp:").append(frame.blockName()).append(" -->");
                }
                continue;
            }
            WpContentNode item = Objects.requireNonNull(frame.nodes().next(), "node");
            if (item instanceof WpHtmlFragment(String html1)) {
                output.append(Objects.requireNonNull(html1, "html"));
            } else if (item instanceof WpBlock block) {
                if (block.name() == null || !nameMatcher.reset(block.name()).matches()) {
                    throw new IllegalArgumentException("Block name must be namespace/name: " + block.name());
                }
                Objects.requireNonNull(block.attributes(), "attributes");
                Objects.requireNonNull(block.content(), "content");
                Objects.requireNonNull(block.syntax(), "syntax");
                String name = block.name().startsWith("core/") ? block.name().substring(5) : block.name();
                output.append("<!-- wp:").append(name);
                if (!block.attributes().isEmpty()) {
                    output.append(' ');
                    appendAttributes(output, block);
                }
                if (block.syntax() == WpBlockSyntax.SELF_CLOSING) {
                    if (!block.content().isEmpty()) {
                        throw new IllegalArgumentException("Self-closing block cannot contain content");
                    }
                    output.append(" /-->");
                } else {
                    output.append(" -->");
                    pending.push(new Frame(block.content().iterator(), name));
                }
            }
        }
        return output.toString();
    }

    private void appendAttributes(StringBuilder output, WpBlock block) {
        try {
            String json = mapper.writeValueAsString(block.attributes());
            // Substitute original JSON tokens once, so introduced escapes are not re-escaped.
            for (int i = 0; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '\\' && i + 1 < json.length()
                    && (json.charAt(i + 1) == '\\' || json.charAt(i + 1) == '"')) {
                    output.append(json.charAt(++i) == '\\' ? "\\u005c" : "\\u0022");
                } else if (c == '-' && i + 1 < json.length() && json.charAt(i + 1) == '-') {
                    output.append("\\u002d\\u002d");
                    i++;
                } else {
                    switch (c) {
                        case '<' -> output.append("\\u003c");
                        case '>' -> output.append("\\u003e");
                        case '&' -> output.append("\\u0026");
                        default -> output.append(c);
                    }
                }
            }
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Block attributes cannot be serialized as JSON", exception);
        }
    }

    // One traversal frame per nesting level, independent of sibling count and list implementation.
    private record Frame(Iterator<WpContentNode> nodes, String blockName) {}
}
