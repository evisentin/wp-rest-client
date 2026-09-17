package io.github.evisentin.wordpress.rest.client.gutenberg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Internal iterative parser preserving literal HTML between block delimiters.
 */
final class GutenbergParser implements WpBlockParser {
    private static final Pattern TOKEN = Pattern.compile("^(/?)wp:([a-z][a-z0-9_-]*(?:/[a-z][a-z0-9_-]*)?)(?:\\s+(.*))?$", Pattern.DOTALL);
    private static final TypeReference<Map<String, Object>> ATTRIBUTES = new TypeReference<>() {};
    private final ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    @Override
    public WpBlockDocument parse(String rawContent) {
        Objects.requireNonNull(rawContent, "rawContent");
        List<WpContentNode> root = new ArrayList<>();
        Deque<Frame> stack = new ArrayDeque<>();
        var matcher = TOKEN.matcher(rawContent);
        int scan = 0;
        int literalStart = 0;
        while (true) {
            int start = rawContent.indexOf("<!--", scan);
            if (start < 0) {
                break;
            }
            int end = rawContent.indexOf("-->", start + 4);
            int bodyStart = start + 4;
            int bodyEnd = end < 0 ? rawContent.length() : end;
            while (bodyStart < bodyEnd && Character.isWhitespace(rawContent.charAt(bodyStart))) {
                bodyStart++;
            }
            boolean blockComment = rawContent.startsWith("wp:", bodyStart)
                                   || rawContent.startsWith("/wp:", bodyStart);
            if (end < 0) {
                if (blockComment) {
                    throw error("Unterminated block comment", start);
                }
                break;
            }
            scan = end + 3;
            if (!blockComment) {
                continue;
            }
            while (bodyEnd > bodyStart && Character.isWhitespace(rawContent.charAt(bodyEnd - 1))) {
                bodyEnd--;
            }
            matcher.region(bodyStart, bodyEnd);
            if (!matcher.matches()) {
                throw error("Invalid block delimiter", start);
            }
            List<WpContentNode> target = stack.isEmpty() ? root : stack.peek().content;
            appendHtml(target, rawContent.substring(literalStart, start));
            boolean closing = matcher.end(1) > matcher.start(1);
            String name = matcher.group(2);
            name = name.contains("/") ? name : "core/" + name;
            String tail = matcher.group(3);
            tail = tail == null ? "" : tail.strip();
            if (closing) {
                if (!tail.isEmpty() || stack.isEmpty() || !stack.peek().name.equals(name)) {
                    throw error("Unexpected closing block: " + name, start);
                }
                Frame frame = stack.pop();
                target = stack.isEmpty() ? root : stack.peek().content;
                target.add(new WpBlock(name, frame.attributes, List.copyOf(frame.content), WpBlockSyntax.PAIRED));
            } else {
                boolean selfClosing = tail.endsWith("/");
                if (selfClosing) {
                    tail = tail.substring(0, tail.length() - 1).stripTrailing();
                }
                Map<String, Object> attributes = readAttributes(tail, start);
                if (selfClosing) {
                    target.add(new WpBlock(name, attributes, List.of(), WpBlockSyntax.SELF_CLOSING));
                } else {
                    stack.push(new Frame(name, attributes, start));
                }
            }
            literalStart = scan;
        }
        if (!stack.isEmpty()) {
            throw error("Unclosed block: " + stack.peek().name, stack.peek().offset);
        }
        appendHtml(root, rawContent.substring(literalStart));
        return new WpBlockDocument(List.copyOf(root));
    }

    private Map<String, Object> readAttributes(String json, int offset) {
        if (json.isEmpty()) {
            return Map.of();
        }
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

    private static WpBlockParseException error(String message, int offset) {
        return new WpBlockParseException(message, offset);
    }

    private static final class Frame {
        private final String name;
        private final Map<String, Object> attributes;
        private final int offset;
        private final List<WpContentNode> content = new ArrayList<>();

        private Frame(String name, Map<String, Object> attributes, int offset) {
            this.name = name;
            this.attributes = attributes;
            this.offset = offset;
        }
    }
}
