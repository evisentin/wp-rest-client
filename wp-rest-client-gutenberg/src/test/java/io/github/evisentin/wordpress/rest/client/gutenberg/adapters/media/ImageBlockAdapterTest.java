package io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media;

import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.media.WpImageBlock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.evisentin.wordpress.rest.client.gutenberg.AdapterTestSupport.block;
import static io.github.evisentin.wordpress.rest.client.gutenberg.AdapterTestSupport.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ImageBlockAdapter")
class ImageBlockAdapterTest {
    private final ImageBlockAdapter adapter = new ImageBlockAdapter();

    @ParameterizedTest
    @ValueSource(strings = {"<figure></figure>", "<figure><img src='a'><img src='b'></figure>"})
    @DisplayName("fromBlock requires exactly one image")
    void fromBlock__fails__when_imageIsMissingOrAmbiguous(String markup) {
        assertThatThrownBy(() -> adapter.fromBlock(block("<!-- wp:image -->" + markup + "<!-- /wp:image -->")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "1.5", "\"42\""})
    @DisplayName("fromBlock rejects nonpositive, fractional and nonnumeric image IDs")
    void fromBlock__fails__when_mediaIdIsInvalid(String id) {
        assertThatThrownBy(() -> adapter.fromBlock(block("<!-- wp:image {\"id\":" + id + "} -->"
                                                         + "<figure><img src=\"x.jpg\"></figure><!-- /wp:image -->")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toBlock rejects adding a caption to a bare image")
    void toBlock__fails__when_captionHasNoFigure() {
        var original = adapter.fromBlock(block("<!-- wp:image --><img src=\"x.jpg\"><!-- /wp:image -->"));
        assertThatThrownBy(() -> adapter.toBlock(new WpImageBlock(null, "x.jpg", "", "Caption", original.attributes(), original.source())))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("figure");
    }

    @ParameterizedTest
    @MethodSource("invalidModels")
    @DisplayName("toBlock rejects invalid IDs, size slugs and unsupported link destinations")
    void toBlock__fails__when_newImageOptionsAreInvalid(WpImageBlock model, String message) {
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(model)).withMessage(message);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("nullUrl")
    @DisplayName("toBlock rejects null models and null image URLs")
    void toBlock__fails__when_requiredModelDataIsNull(WpImageBlock model) {
        assertThatNullPointerException().isThrownBy(() -> adapter.toBlock(model));
    }

    @Test
    @DisplayName("toBlock rejects changes to existing image options")
    void toBlock__fails__when_sourceOptionsChange() {
        var source = block("<!-- wp:image --><figure><img src='x.jpg'></figure><!-- /wp:image -->");
        assertThatIllegalArgumentException().isThrownBy(() -> adapter.toBlock(new WpImageBlock(null, "x.jpg", "", null, Map.of("sizeSlug", "large"), source)))
                                            .withMessageContaining("Changing saved block options");
    }

    @Test
    @DisplayName("toBlock removes the caption and media ID while preserving custom classes and links")
    void toBlock__succeeds__when_captionAndMediaIdAreRemoved() {
        var original = adapter.fromBlock(block("<!-- wp:image {\"id\":42,\"linkDestination\":\"custom\"} -->"
                                               + "<figure><a href=\"https://example.com/page\"><img src=\"old.jpg\" class=\"custom wp-image-42\" "
                                               + "srcset=\"old.jpg 1x\" sizes=\"100vw\" alt=\"old\"></a><figcaption>Caption</figcaption></figure><!-- /wp:image -->"));
        var updated = adapter.toBlock(new WpImageBlock(null, "new.jpg", null, null, original.attributes(), original.source()));
        var document = html(updated);
        assertThat(updated.attributes()).doesNotContainKey("id");
        assertThat(document.select("figcaption")).isEmpty();
        assertThat(document.selectFirst("img").classNames()).containsExactly("custom");
        assertThat(document.selectFirst("img").hasAttr("srcset")).isFalse();
        assertThat(document.selectFirst("img").hasAttr("sizes")).isFalse();
        assertThat(document.selectFirst("img").attr("alt")).isEmpty();
        assertThat(document.selectFirst("a").attr("href")).isEqualTo("https://example.com/page");
    }

    @Test
    @DisplayName("toBlock adds a caption while preserving responsive sources for an unchanged URL")
    void toBlock__succeeds__when_captionChangesAndUrlIsUnchanged() {
        var original = adapter.fromBlock(block("<!-- wp:image --><figure><img src=\"x.jpg\" srcset=\"x.jpg 1x\" "
                                               + "sizes=\"100vw\" alt=\"\"></figure><!-- /wp:image -->"));
        var updated = adapter.toBlock(new WpImageBlock(null, "x.jpg", "alt", "<em>Caption</em>", original.attributes(), original.source()));
        var document = html(updated);
        assertThat(document.selectFirst("figcaption").html()).isEqualTo("<em>Caption</em>");
        assertThat(document.selectFirst("figcaption").classNames()).contains("wp-element-caption");
        assertThat(document.selectFirst("img").attr("srcset")).isEqualTo("x.jpg 1x");
        assertThat(document.selectFirst("img").attr("sizes")).isEqualTo("100vw");
    }

    @Test
    @DisplayName("toBlock changes an unlinked media image URL without requiring an anchor")
    void toBlock__succeeds__when_mediaDestinationHasNoLink() {
        var source = block("<!-- wp:image {\"linkDestination\":\"media\"} --><figure><img src='old.jpg'></figure><!-- /wp:image -->");
        var result = adapter.toBlock(new WpImageBlock(null, "new.jpg", "", null, source.attributes(), source));
        assertThat(html(result).selectFirst("img").attr("src")).isEqualTo("new.jpg");
        assertThat(html(result).select("a")).isEmpty();
    }

    @Test
    @DisplayName("toBlock updates the media link, ID and caption without losing custom markup")
    void toBlock__succeeds__when_mediaIdUrlAndCaptionChange() {
        WpBlock source = block("<!-- wp:image {\"id\":42,\"linkDestination\":\"media\"} -->"
                               + "<figure class=\"wp-block-image\"><a href=\"old.jpg\"><img src=\"old.jpg\" alt=\"Old\" "
                               + "class=\"custom wp-image-42\" srcset=\"old.jpg 1x\"/></a><figcaption>Old caption</figcaption></figure><!-- /wp:image -->");
        var model = adapter.fromBlock(source);
        assertThat(adapter.toBlock(model)).isSameAs(source);
        var updated = adapter.toBlock(new WpImageBlock(43L, "new.jpg?a=1&b=2", "New \"alt\"", "<em>Caption</em>",
                model.attributes(), model.source()));
        assertThat(updated.attributes()).containsEntry("id", 43L);
        assertThat(serialize(updated)).contains("wp-image-43", "custom", "<em>Caption</em>", "href=\"new.jpg?a=1&amp;b=2\"")
                                      .doesNotContain("srcset", "wp-image-42");
        assertThat(adapter.fromBlock(updated).altText()).isEqualTo("New \"alt\"");
    }

    @Test
    @DisplayName("toBlock returns the original block for an unchanged image")
    void toBlock__succeeds__when_modelIsUnchanged() {
        var source = block("<!-- wp:image --><figure><img src='x.jpg'></figure><!-- /wp:image -->");
        assertThat(adapter.toBlock(adapter.fromBlock(source))).isSameAs(source);
    }

    @Test
    @DisplayName("toBlock creates an image without an ID, caption or optional options")
    void toBlock__succeeds__when_newImageHasOnlyRequiredFields() {
        var result = adapter.toBlock(new WpImageBlock(null, "x.jpg", null, null, Map.of()));
        assertThat(result.attributes()).isEmpty();
        assertThat(html(result).selectFirst("img").outerHtml()).isEqualTo("<img src=\"x.jpg\" alt=\"\">");
        assertThat(html(result).select("figcaption")).isEmpty();
    }

    @Test
    @DisplayName("toBlock creates an image with supported options and escaped attribute text")
    void toBlock__succeeds__when_newImageHasSupportedOptions() {
        var result = adapter.toBlock(new WpImageBlock(2L, "image.jpg", "a&b", "<em>Caption</em>",
                Map.of("className", "custom", "anchor", "photo", "sizeSlug", "large", "linkDestination", "none")));
        var document = html(result);
        assertThat(document.selectFirst("figure").classNames()).containsExactlyInAnyOrder("wp-block-image", "custom", "size-large");
        assertThat(document.selectFirst("figure").id()).isEqualTo("photo");
        assertThat(document.selectFirst("img").attr("alt")).isEqualTo("a&b");
        assertThat(document.selectFirst("img").outerHtml()).contains("a&amp;b", "wp-image-2");
        assertThat(document.selectFirst("figcaption").html()).isEqualTo("<em>Caption</em>");
        assertThat(result.attributes()).containsEntry("id", 2L);
    }

    @Test
    @DisplayName("toBlock removes the class attribute when the media ID was the only class")
    void toBlock__succeeds__when_removingTheOnlyImageClass() {
        var source = block("<!-- wp:image {\"id\":42} --><figure><img src='x.jpg' class='wp-image-42'></figure><!-- /wp:image -->");
        var result = adapter.toBlock(new WpImageBlock(null, "x.jpg", "", "", Map.of(), source));
        assertThat(html(result).selectFirst("img").hasAttr("class")).isFalse();
        assertThat(result.attributes()).doesNotContainKey("id");
    }

    private String serialize(WpBlock block) {
        return new DefaultWpGutenbergCodec().serialize(new WpBlockDocument(List.of(block)));
    }

    static Stream<Arguments> invalidModels() {
        return Stream.of(Arguments.of(new WpImageBlock(0L, "x", "", null, Map.of()), "Image ID must be positive"),
                Arguments.of(new WpImageBlock(-1L, "x", "", null, Map.of()), "Image ID must be positive"),
                Arguments.of(new WpImageBlock(null, "x", "", null, Map.of("sizeSlug", "bad slug")), "Invalid image size slug"),
                Arguments.of(new WpImageBlock(null, "x", "", null, Map.of("linkDestination", "media")), "New linked images require explicit saved markup"));
    }

    static Stream<WpImageBlock> nullUrl() {return Stream.of(new WpImageBlock(null, null, "", null, Map.of()));}
}
