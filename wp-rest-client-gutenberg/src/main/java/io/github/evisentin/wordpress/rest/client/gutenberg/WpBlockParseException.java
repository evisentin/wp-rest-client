package io.github.evisentin.wordpress.rest.client.gutenberg;

import lombok.Getter;

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
@Getter
public class WpBlockParseException extends RuntimeException {

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
}
