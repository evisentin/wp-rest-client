package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class GutenbergTest {
    @Test
    void createsContentWithEscapedTextRichHtmlAndUsefulDefaults() {
        var content = Gutenberg.document()
                               .heading("Welcome <reader> & friends")
                               .paragraph("<strong>plain</strong>")
                               .paragraph(p -> p.html("Hello <strong>world</strong>").className("intro").anchor("hello"))
                               .heading(h -> h.text("Section").level(3).className("section").anchor("section"))
                               .image(i -> i.id(42).url("https://example.com/photo.jpg").alt("A mountain")
                                            .captionHtml("A <em>caption</em>").className("photo").anchor("photo"));
        String raw = content.serialize();
        assertThat(raw).contains("<h2 class=\"wp-block-heading\">Welcome &lt;reader&gt; &amp; friends</h2>",
                "<p>&lt;strong&gt;plain&lt;/strong&gt;</p>", "Hello <strong>world</strong>",
                "<h3", "wp-image-42", "A <em>caption</em>", "id=\"hello\"", "class=\"intro\"");
        assertThat(Gutenberg.parse(raw).serialize()).isEqualTo(raw);
        assertThat(content.serialize(true)).contains("-->\n<h2");
        assertThat(Gutenberg.document().serialize()).isEmpty();
    }

    @Test
    void editsDeeplyNestedDocumentsWithoutUsingTheCallStack() {
        String raw = "<!-- wp:group -->".repeat(2000)
                     + Gutenberg.document().paragraph("Before").serialize()
                     + "<!-- /wp:group -->".repeat(2000);
        assertThat(Gutenberg.parse(raw).editParagraphs(p -> p.text("After")).serialize())
                .isEqualTo(raw.replace("Before", "After"));
        assertThat(Gutenberg.document().editParagraphs(p -> fail("No paragraphs")).serialize()).isEmpty();
    }

    @Test
    void editsImagesPreservingLinksAndClearingStaleResponsiveAttributes() {
        var document = Gutenberg.parse("<!-- wp:image {\"id\":1,\"linkDestination\":\"media\"} -->"
                                       + "<figure data-keep='yes'><a href='old.jpg'><img src='old.jpg' alt='Old' class='wp-image-1' "
                                       + "srcset='old-2.jpg 2x' sizes='100vw'></a><figcaption>Old caption</figcaption></figure><!-- /wp:image -->");
        document.editImages(i -> {
            assertThat(i.id()).isEqualTo(1);
            assertThat(i.url()).isEqualTo("old.jpg");
            assertThat(i.alt()).isEqualTo("Old");
            assertThat(i.captionHtml()).isEqualTo("Old caption");
            i.id(2L).url("new.jpg").alt("New").captionHtml("<b>New</b>");
        });
        assertThat(document.serialize()).contains("href=\"new.jpg\"", "wp-image-2", "data-keep=\"yes\"", "<b>New</b>")
                                        .doesNotContain("srcset", "sizes=");
        document.editImages(i -> i.id((Long) null).captionHtml(null));
        assertThat(document.serialize()).doesNotContain("wp-image-", "figcaption");
    }

    @Test
    void editsNestedContentAndPreservesUnknownBlocksWrappersAndWhitespace() {
        String raw = "before\n<!-- wp:plugin/wrapper {\"custom\":true} --><section data-x='keep'>\n"
                     + "<!-- wp:paragraph --><p data-extra='yes'>Old <strong>name</strong></p><!-- /wp:paragraph -->"
                     + "<!-- wp:heading --><h2 class='wp-block-heading'>Old heading</h2><!-- /wp:heading -->"
                     + "</section><!-- /wp:plugin/wrapper -->\nafter<!-- wp:plugin/dynamic /-->";
        var document = Gutenberg.parse(raw);
        var original = document.build();
        var calls = new ArrayList<String>();
        document.editParagraphs(p -> {
                    calls.add(p.html());
                    p.html(p.html().replace("Old", "New"));
                })
                .editHeadings(h -> {
                    assertThat(h.level()).isEqualTo(2);
                    h.html(h.html().replace("Old", "New")).level(3);
                });
        assertThat(calls).containsExactly("Old <strong>name</strong>");
        assertThat(document.serialize()).contains("<section data-x='keep'>\n", "data-extra=\"yes\"",
                "New <strong>name</strong>", "<h3 class=\"wp-block-heading\">New heading</h3>",
                "</section><!-- /wp:plugin/wrapper -->\nafter<!-- wp:plugin/dynamic /-->");
        assertThat(document.build().nodes().getLast()).isSameAs(original.nodes().getLast());
        assertThat(((WpBlock) document.build().nodes().get(1)).attributes()).containsEntry("custom", true);
        assertThat(new DefaultWpGutenbergCodec().serialize(original)).isEqualTo(raw);
    }

    @Test
    void failedEditsAreAtomicAndInvalidConfigurationIsRejected() {
        var document = Gutenberg.document().heading("One").heading("Two");
        var original = document.build();
        assertThatIllegalArgumentException().isThrownBy(() -> document.editHeadings(h -> h.level(h.html().equals("Two") ? 7 : 3)));
        assertThat(document.build()).isEqualTo(original);
        assertThatIllegalArgumentException().isThrownBy(() -> document.editHeadings(h -> h.anchor("changed")));
        assertThat(document.build()).isEqualTo(original);
        assertThatIllegalArgumentException().isThrownBy(() -> document.image(i -> i.id(0).url("photo.jpg")));
        assertThatNullPointerException().isThrownBy(() -> document.image(i -> {}));
        assertThatNullPointerException().isThrownBy(() -> document.node(null));
        assertThatNullPointerException().isThrownBy(() -> document.paragraph((String) null));
        assertThatNullPointerException().isThrownBy(() -> document.editParagraphs(null));
        assertThatNullPointerException().isThrownBy(() -> document.editHeadings(null));
        assertThatNullPointerException().isThrownBy(() -> document.editImages(null));
        assertThatExceptionOfType(WpBlockParseException.class).isThrownBy(() -> Gutenberg.parse("<!-- wp:paragraph -->"));
    }

    @Test
    void noOpEditsPreserveSourceIdentityAndSnapshotsSurviveAppends() {
        var document = Gutenberg.document().paragraph(p -> p.text("Hello"))
                                .heading(h -> h.text("Heading")).image(i -> i.url("photo.jpg"))
                                .node(new WpHtmlFragment("\n"));
        var snapshot = document.build();
        document.editParagraphs(p -> {}).editHeadings(h -> {}).editImages(i -> {});
        for (int i = 0; i < snapshot.nodes().size(); i++) {
            assertThat(document.build().nodes().get(i)).isSameAs(snapshot.nodes().get(i));
        }
        document.paragraph("Later");
        assertThat(snapshot.nodes()).hasSize(4);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> snapshot.nodes().clear());
    }
}
