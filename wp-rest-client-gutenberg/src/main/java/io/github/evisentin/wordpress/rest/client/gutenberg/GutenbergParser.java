package io.github.evisentin.wordpress.rest.client.gutenberg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;
import lombok.NonNull;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

/**
 * Internal iterative parser preserving literal HTML between block delimiters.
 */
final class GutenbergParser implements WpBlockParser {

    private static final Pattern TOKEN = Pattern.compile("^(/?)wp:([a-z][a-z0-9_-]*(?:/[a-z][a-z0-9_-]*)?)(?:\\s+(.*))?$", DOTALL);
    private static final TypeReference<Map<String, Object>> ATTRIBUTES = new TypeReference<>() {};

    private final ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    @Override
    public WpBlockDocument parse(@NonNull String rawContent) {
        List<WpContentNode> root = new ArrayList<>();
        Deque<Frame> stack = new ArrayDeque<>();

        int scan = 0;
        int literalStart = 0;

        while (true) {
            Delimiter delimiter = findNextDelimiter(rawContent, scan);
            if (delimiter == null) break;

            appendHtml(currentContent(root, stack), rawContent.substring(literalStart, delimiter.start()));
            processDelimiter(delimiter, root, stack);

            scan = delimiter.end();
            literalStart = scan;
        }

        ensureAllBlocksClosed(stack);
        appendHtml(root, rawContent.substring(literalStart));

        return new WpBlockDocument(List.copyOf(root));
    }

    private void appendSelfClosingBlock(Delimiter delimiter, List<WpContentNode> root, Deque<Frame> stack) {
        Map<String, Object> attributes = readAttributes(delimiter.tail(), delimiter.start());
        WpBlock block = new WpBlock(delimiter.name(), attributes, List.of(), WpBlockSyntax.SELF_CLOSING);

        currentContent(root, stack).add(block);
    }

    private void closeBlock(Delimiter delimiter, List<WpContentNode> root, Deque<Frame> stack) {
        if (!delimiter.tail().isEmpty() || stack.isEmpty() || !stack.peek().name().equals(delimiter.name())) {
            throw error("Unexpected closing block: " + delimiter.name(), delimiter.start());
        }

        Frame frame = stack.pop();
        WpBlock block = new WpBlock(frame.name(), frame.attributes(), List.copyOf(frame.content()), WpBlockSyntax.PAIRED);

        currentContent(root, stack).add(block);
    }

    private Delimiter findNextDelimiter(String content, int from) {
        int scan = from;

        while (true) {
            int start = content.indexOf("<!--", scan);
            if (start < 0) return null;

            int end = content.indexOf("-->", start + 4);
            int bodyEnd = end < 0 ? content.length() : end;
            int bodyStart = skipLeadingWhitespace(content, start + 4, bodyEnd);

            if (!isBlockComment(content, bodyStart)) {
                if (end < 0) return null;

                scan = end + 3;
                continue;
            }

            if (end < 0) {
                throw error("Unterminated block comment", start);
            }

            return parseDelimiter(content, start, end, bodyStart);
        }
    }

    private void openBlock(Delimiter delimiter, Deque<Frame> stack) {
        Map<String, Object> attributes = readAttributes(delimiter.tail(), delimiter.start());
        stack.push(new Frame(delimiter.name(), attributes, delimiter.start()));
    }

    private Delimiter parseDelimiter(String content, int start, int end, int bodyStart) {
        int bodyEnd = skipTrailingWhitespace(content, bodyStart, end);

        var matcher = TOKEN.matcher(content);
        matcher.region(bodyStart, bodyEnd);

        if (!matcher.matches()) {
            throw error("Invalid block delimiter", start);
        }

        String name = normalizeBlockName(matcher.group(2));
        String tail = Objects.toString(matcher.group(3), "").strip();

        DelimiterType type;

        if (!matcher.group(1).isEmpty()) {
            type = DelimiterType.CLOSE;
        } else if (tail.endsWith("/")) {
            type = DelimiterType.SELF_CLOSING;
            tail = tail.substring(0, tail.length() - 1).stripTrailing();
        } else {
            type = DelimiterType.OPEN;
        }

        return new Delimiter(name, tail, type, start, end + 3);
    }

    private void processDelimiter(Delimiter delimiter, List<WpContentNode> root, Deque<Frame> stack) {
        switch (delimiter.type()) {
            case OPEN -> openBlock(delimiter, stack);
            case CLOSE -> closeBlock(delimiter, root, stack);
            case SELF_CLOSING -> appendSelfClosingBlock(delimiter, root, stack);
        }
    }

    private Map<String, Object> readAttributes(String json, int offset) {
        if (json.isEmpty()) return Map.of();

        if (!json.startsWith("{")) {
            throw error("Block attributes must be a JSON object", offset);
        }

        try {
            return mapper.readValue(json, ATTRIBUTES);
        } catch (JsonProcessingException exception) {
            throw new WpBlockParseException("Invalid block attributes", offset, exception);
        }
    }

    private static void appendHtml(List<WpContentNode> nodes, String html) {
        if (!html.isEmpty()) {
            nodes.add(new WpHtmlFragment(html));
        }
    }

    private static List<WpContentNode> currentContent(List<WpContentNode> root, Deque<Frame> stack) {
        return stack.isEmpty() ? root : stack.peek().content();
    }

    private static void ensureAllBlocksClosed(Deque<Frame> stack) {
        if (stack.isEmpty()) return;

        Frame frame = stack.peek();
        throw error("Unclosed block: " + frame.name(), frame.offset());
    }

    private static WpBlockParseException error(String message, int offset) {
        return new WpBlockParseException(message, offset);
    }

    private static boolean isBlockComment(String content, int offset) {
        return content.startsWith("wp:", offset) || content.startsWith("/wp:", offset);
    }

    private static String normalizeBlockName(String name) {
        return name.contains("/") ? name : "core/" + name;
    }

    private static int skipLeadingWhitespace(String content, int start, int end) {
        while (start < end && Character.isWhitespace(content.charAt(start))) {start++;}
        return start;
    }

    private static int skipTrailingWhitespace(String content, int start, int end) {
        while (end > start && Character.isWhitespace(content.charAt(end - 1))) {end--;}
        return end;
    }

    private enum DelimiterType {
        OPEN,
        CLOSE,
        SELF_CLOSING
    }

    private record Delimiter(String name, String tail, DelimiterType type, int start, int end) {}

    private record Frame(String name, Map<String, Object> attributes, int offset, List<WpContentNode> content) {

        private Frame(String name, Map<String, Object> attributes, int offset) {
            this(name, attributes, offset, new ArrayList<>());
        }
    }
}
