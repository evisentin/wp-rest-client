package io.github.evisentin.wordpress.rest.client.gutenberg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Internal serializer for stored block markup; does not execute Gutenberg save functions.
 */
final class GutenbergSerializer implements WpBlockSerializer {

    private static final Pattern BLOCK_NAME = Pattern.compile("[a-z][a-z0-9_-]*/[a-z][a-z0-9_-]*");

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize(WpBlockDocument document) {
        Objects.requireNonNull(document, "document");

        StringBuilder output = new StringBuilder();
        Deque<Frame> stack = new ArrayDeque<>();
        stack.push(Frame.root(Objects.requireNonNull(document.nodes(), "nodes").iterator()));

        while (!stack.isEmpty()) {
            processFrame(stack, output);
        }

        return output.toString();
    }

    private void appendAttributes(WpBlock block, StringBuilder output) {
        if (block.attributes().isEmpty()) {
            return;
        }

        output.append(' ');
        appendAttributesJson(block.attributes(), output);
    }

    private void appendAttributesJson(Object attributes, StringBuilder output) {
        try {
            String json = mapper.writeValueAsString(attributes);
            appendEscapedJson(json, output);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Block attributes cannot be serialized as JSON", exception);
        }
    }

    private void appendBlock(WpBlock block, Deque<Frame> stack, StringBuilder output) {
        validateBlock(block);

        String name = serializedName(block.name());

        output.append("<!-- wp:").append(name);
        appendAttributes(block, output);

        if (block.syntax() == WpBlockSyntax.SELF_CLOSING) {
            output.append(" /-->");
            return;
        }

        output.append(" -->");
        stack.push(new Frame(block.content().iterator(), name));
    }

    private void processFrame(Deque<Frame> stack, StringBuilder output) {
        Frame frame = stack.peek();

        if (!frame.nodes().hasNext()) {
            closeFrame(stack, output);
            return;
        }

        WpContentNode node = Objects.requireNonNull(frame.nodes().next(), "node");
        serializeNode(node, stack, output);
    }

    private void serializeNode(WpContentNode node, Deque<Frame> stack, StringBuilder output) {
        if (node instanceof WpHtmlFragment fragment) {
            appendHtml(fragment, output);
            return;
        }

        if (node instanceof WpBlock block) {
            appendBlock(block, stack, output);
            return;
        }

        throw new IllegalArgumentException("Unsupported content node: " + node.getClass().getName());
    }

    private static void appendEscapedCharacter(char character, StringBuilder output) {
        switch (character) {
            case '<' -> output.append("\\u003c");
            case '>' -> output.append("\\u003e");
            case '&' -> output.append("\\u0026");
            default -> output.append(character);
        }
    }

    private static void appendEscapedJson(String json, StringBuilder output) {
        for (int i = 0; i < json.length(); i++) {
            char current = json.charAt(i);

            if (isEscapedBackslashOrQuote(json, i)) {
                char escaped = json.charAt(++i);
                output.append(escaped == '\\' ? "\\u005c" : "\\u0022");
                continue;
            }

            if (isDoubleHyphen(json, i)) {
                output.append("\\u002d\\u002d");
                i++;
                continue;
            }

            appendEscapedCharacter(current, output);
        }
    }

    private static void appendHtml(WpHtmlFragment fragment, StringBuilder output) {
        output.append(Objects.requireNonNull(fragment.html(), "html"));
    }

    private static void closeFrame(Deque<Frame> stack, StringBuilder output) {
        Frame frame = stack.pop();

        if (frame.blockName() != null) {
            output.append("<!-- /wp:").append(frame.blockName()).append(" -->");
        }
    }

    private static boolean isDoubleHyphen(String json, int index) {
        return json.charAt(index) == '-' && index + 1 < json.length() && json.charAt(index + 1) == '-';
    }

    private static boolean isEscapedBackslashOrQuote(String json, int index) {
        if (json.charAt(index) != '\\' || index + 1 >= json.length()) {
            return false;
        }

        char next = json.charAt(index + 1);
        return next == '\\' || next == '"';
    }

    private static String serializedName(String name) {
        return name.startsWith("core/") ? name.substring(5) : name;
    }

    private static void validateBlock(WpBlock block) {
        String name = block.name();

        if (name == null || !BLOCK_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("Block name must be namespace/name: " + name);
        }

        Objects.requireNonNull(block.attributes(), "attributes");
        Objects.requireNonNull(block.content(), "content");
        Objects.requireNonNull(block.syntax(), "syntax");

        if (block.syntax() == WpBlockSyntax.SELF_CLOSING && !block.content().isEmpty()) {
            throw new IllegalArgumentException("Self-closing block cannot contain content");
        }
    }

    /**
     * One traversal frame per nesting level, independent of sibling count and list implementation.
     */
    private record Frame(Iterator<WpContentNode> nodes, String blockName) {

        private static Frame root(Iterator<WpContentNode> nodes) {
            return new Frame(nodes, null);
        }
    }
}
