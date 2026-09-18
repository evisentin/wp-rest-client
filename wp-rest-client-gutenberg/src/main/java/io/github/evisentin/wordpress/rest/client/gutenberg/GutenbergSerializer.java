package io.github.evisentin.wordpress.rest.client.gutenberg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Internal serializer for stored block markup; does not execute Gutenberg save functions.
 */
final class GutenbergSerializer implements WpBlockSerializer {

    private static final Pattern BLOCK_NAME = Pattern.compile("[a-z][a-z0-9_-]*/[a-z][a-z0-9_-]*");
    private static final String CORE_NAMESPACE = "core/";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String serialize(@NonNull WpBlockDocument document) {
        Deque<Frame> stack = new ArrayDeque<>();
        StringBuilder output = new StringBuilder();

        stack.push(Frame.root(document.nodes().iterator()));

        while (!stack.isEmpty()) {
            processFrame(stack, output);
        }

        return output.toString();
    }

    private void appendAttributes(Map<String, Object> attributes, StringBuilder output) {
        if (attributes.isEmpty()) return;

        output.append(' ');

        try {
            appendEscapedJson(mapper.writeValueAsString(attributes), output);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Block attributes cannot be serialized as JSON", exception);
        }
    }

    private void appendBlock(WpBlock block, Deque<Frame> stack, StringBuilder output) {
        validateBlock(block);

        String name = serializedName(block.name());

        output.append("<!-- wp:").append(name);
        appendAttributes(block.attributes(), output);

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

        serializeNode(frame.nodes().next(), stack, output);
    }

    private void serializeNode(@NonNull WpContentNode node, Deque<Frame> stack, StringBuilder output) {
        switch (node) {
            case WpHtmlFragment fragment -> output.append(fragment.html());
            case WpBlock block -> appendBlock(block, stack, output);
        }
    }

    private static void appendEscapedJson(String json, StringBuilder output) {
        // Replace JSON-escaped backslashes before quotes, so literal escape sequences remain literal.
        output.append(json.replace("\\\\", "\\u005c")
                          .replace("\\\"", "\\u0022")
                          .replace("--", "\\u002d\\u002d")
                          .replace("<", "\\u003c")
                          .replace(">", "\\u003e")
                          .replace("&", "\\u0026"));
    }

    private static void closeFrame(Deque<Frame> stack, StringBuilder output) {
        Frame frame = stack.pop();

        if (frame.blockName() != null) {
            output.append("<!-- /wp:").append(frame.blockName()).append(" -->");
        }
    }

    private static String serializedName(String name) {
        return StringUtils.removeStart(name, CORE_NAMESPACE);
    }

    private static void validateBlock(WpBlock block) {
        if (!BLOCK_NAME.matcher(StringUtils.defaultString(block.name())).matches()) {
            throw new IllegalArgumentException("Block name must be namespace/name: " + block.name());
        }

        if (block.attributes() == null) {
            throw new IllegalArgumentException("Block attributes cannot be null");
        }

        if (block.content() == null) {
            throw new IllegalArgumentException("Block content cannot be null");
        }

        if (block.syntax() == null) {
            throw new IllegalArgumentException("Block syntax cannot be null");
        }

        if (block.syntax() == WpBlockSyntax.SELF_CLOSING && !block.content().isEmpty()) {
            throw new IllegalArgumentException("Self-closing block cannot contain content");
        }
    }

    /**
     * One traversal frame per nesting level, independent of sibling count and list implementation.
     */
    private record Frame(Iterator<WpContentNode> nodes, String blockName) {

        static Frame root(Iterator<WpContentNode> nodes) {
            return new Frame(nodes, null);
        }
    }
}
