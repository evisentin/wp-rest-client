package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@DisplayName("DefaultWpGutenbergCodec")
class DefaultWpGutenbergCodecTest {
    private final DefaultWpGutenbergCodec codec = new DefaultWpGutenbergCodec();

    @Test
    @DisplayName("parse propagates malformed-content diagnostics")
    void parse__fails__when_contentIsMalformed() {
        assertThatExceptionOfType(WpBlockParseException.class).isThrownBy(() -> codec.parse("<!-- /wp:paragraph -->"))
                                                              .withMessage("Unexpected closing block: core/paragraph");
    }

    @Test
    @DisplayName("parse exposes the default parser through the public codec")
    void parse__succeeds__when_contentIsValid() {
        assertThat(codec.parse("<!-- wp:paragraph --><p>Hello</p><!-- /wp:paragraph -->").nodes())
                .containsExactly(new WpBlock("core/paragraph", Map.of(), List.of(new WpHtmlFragment("<p>Hello</p>")), WpBlockSyntax.PAIRED));
    }

    @Test
    @DisplayName("serialize propagates invalid-document errors")
    void serialize__fails__when_documentIsNull() {
        assertThatNullPointerException().isThrownBy(() -> codec.serialize(null));
    }

    @Test
    @DisplayName("serialize exposes the default serializer through the public codec")
    void serialize__succeeds__when_documentIsValid() {
        assertThat(codec.serialize(new WpBlockDocument(List.of(new WpHtmlFragment("<p>Hello</p>")))))
                .isEqualTo("<p>Hello</p>");
    }

    @Test
    @DisplayName("parse and serialize handle deeply nested content without recursive traversal")
    void serialize__succeeds__when_parsedContentIsDeeplyNested() {
        String raw = "<!-- wp:group -->".repeat(2000) + "x" + "<!-- /wp:group -->".repeat(2000);
        assertThat(codec.serialize(codec.parse(raw))).isEqualTo(raw);
    }
}
