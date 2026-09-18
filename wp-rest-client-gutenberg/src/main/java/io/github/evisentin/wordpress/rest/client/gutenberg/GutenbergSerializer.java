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
    public String serialize(WpBlockDocument document) {
        return serialize(document, false);
    }

    @Override
    public String serialize(@NonNull WpBlockDocument document, boolean prettyPrint) {
        Deque<Frame> stack = new ArrayDeque<>();
        Output output = new Output(prettyPrint);

        stack.push(Frame.root(document.nodes().iterator()));

        while (!stack.isEmpty()) {
            processFrame(stack, output);
        }

        return output.text.toString();
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

    private void appendBlock(WpBlock block, Deque<Frame> stack, Output output) {
        validateBlock(block);

        String name = serializedName(block.name());

        output.beforeDelimiter();
        output.text.append("<!-- wp:").append(name);
        appendAttributes(block.attributes(), output.text);
        output.afterDelimiter = true;

        if (block.syntax() == WpBlockSyntax.SELF_CLOSING) {
            output.text.append(" /-->");
            return;
        }

        output.text.append(" -->");
        stack.push(new Frame(block.content().iterator(), name));
    }

    private void processFrame(Deque<Frame> stack, Output output) {
        Frame frame = stack.peek();

        if (!frame.nodes().hasNext()) {
            closeFrame(stack, output);
            return;
        }

        serializeNode(frame.nodes().next(), stack, output);
    }

    private void serializeNode(@NonNull WpContentNode node, Deque<Frame> stack, Output output) {
        switch (node) {
            case WpHtmlFragment fragment -> output.appendHtml(fragment.html());
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

    private static void closeFrame(Deque<Frame> stack, Output output) {
        Frame frame = stack.pop();

        if (frame.blockName() != null) {
            output.beforeDelimiter();
            output.text.append("<!-- /wp:").append(frame.blockName()).append(" -->");
            output.afterDelimiter = true;
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
     * Per-call formatting state. Delayed line breaks avoid adding trailing or duplicate newlines.
     */
    private static final class Output {
        private final StringBuilder text = new StringBuilder();
        private final boolean prettyPrint;
        private boolean afterDelimiter;

        private Output(boolean prettyPrint) {
            this.prettyPrint = prettyPrint;
        }

        private void appendHtml(String html) {
            String literal = String.valueOf(html);
            if (literal.isEmpty()) return;
            if (prettyPrint && afterDelimiter && !literal.startsWith("\n") && !literal.startsWith("\r")) {
                lineBreak();
            }
            text.append(literal);
            afterDelimiter = false;
        }

        private void beforeDelimiter() {
            if (prettyPrint) {
                lineBreak();
            }
        }

        private void lineBreak() {
            if (!text.isEmpty() && text.charAt(text.length() - 1) != '\n' && text.charAt(text.length() - 1) != '\r') {
                text.append('\n');
            }
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
