package io.github.evisentin.wordpress.rest.client.gutenberg.model;

/**
 * Reports malformed block syntax at a position in the raw content string.
 *
 * <p>Example malformed input: the closing block name does not match the opening name:</p>
 * <pre>{@code
 * <!-- wp:paragraph -->
 * <p>Hello world.</p>
 * <!-- /wp:heading -->
 * }</pre>
 * <p>A strict parser reports this mismatch using this exception.</p>
 */
public class WpBlockParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int offset;

    /**
     * @param message
     *         explanation of the syntax error
     * @param offset
     *         zero-based UTF-16 character offset in the input string
     */
    public WpBlockParseException(String message, int offset) {
        super(message);
        this.offset = offset;
    }

    /**
     * @param message
     *         explanation of the syntax error
     * @param offset
     *         zero-based UTF-16 character offset in the input string
     * @param cause
     *         underlying parsing error
     */
    public WpBlockParseException(String message, int offset, Throwable cause) {
        super(message, cause);
        this.offset = offset;
    }

    /**
     * @return zero-based UTF-16 character offset in the input string
     */
    public int getOffset() {
        return offset;
    }
}
