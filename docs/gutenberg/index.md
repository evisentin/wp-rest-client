# Working with Gutenberg Content

The optional `wp-rest-client-gutenberg` module lets you read, create, and edit the block markup stored in WordPress
posts and pages. Use it to inspect block trees, update supported block content, or build raw content to send through
the REST client.

It works with stored Gutenberg markup (`content.raw`). It does not render blocks, execute JavaScript save functions,
or convert rendered HTML back into editable blocks. The codec can also be used independently of an HTTP client.

## Installation

Add the Gutenberg module alongside your chosen [HTTP client implementation](../getting_started/installation.md).
Use a version that includes this module, aligned with the other WordPress REST Client dependencies in your application.
The project's [Java requirements](../getting_started/installation.md#requirements) also apply here.

=== "Maven"
    ```xml title="pom.xml"
    <dependency>
      <groupId>io.github.evisentin</groupId>
      <artifactId>wp-rest-client-gutenberg</artifactId>
      <version>::latest::</version>
    </dependency>
    ```
=== "Gradle (groovy)"
    ```groovy title="build.gradle"
    dependencies {
        implementation 'io.github.evisentin:wp-rest-client-gutenberg:<latest version>'
    }
    ```
=== "Gradle (kotlin)"
    ```kotlin title="build.gradle.kts"
    dependencies {
        implementation("io.github.evisentin:wp-rest-client-gutenberg:<latest version>")
    }
    ```

## Fluent Content API

Use `Gutenberg` for creating paragraphs, headings and images, or editing those blocks in existing content.
The facade handles adapter conversion and document node replacement for you.

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.Gutenberg;

String rawContent = Gutenberg.document()
        .heading("Welcome") // defaults to h2
        .paragraph(p -> p.html("Hello <strong>world</strong>").className("intro"))
        .heading(h -> h.text("Photos").level(3).anchor("photos"))
        .image(i -> i.id(42).url("https://example.com/photo.jpg")
                .alt("A mountain landscape").captionHtml("Mountain <em>view</em>"))
        .serialize();
```

`heading(String)` and `paragraph(String)` take plain text, as does `.text(...)` inside a callback.
Plain text is HTML-escaped; `.html(...)` and `.captionHtml(...)` accept trusted rich-text HTML and do not sanitize it.
Headings default to level 2; paragraph and heading content defaults to empty text. Images require a URL;
the media ID and caption are optional, and alternative text defaults to empty.

To edit a document, start with `content.raw`:

```java
String updated = Gutenberg.parse(rawContent)
        .editParagraphs(p -> p.html(p.html().replace("world", "WordPress")))
        .editHeadings(h -> h.level(3))
        .editImages(i -> i.alt("Updated description"))
        .serialize();
```

Each edit visits **all matching blocks, including nested blocks**, in document order. Callbacks expose the current
fields: `html()` for text blocks, `level()` for headings, and `id()`, `url()`, `alt()` and `captionHtml()` for images.
Use conditional logic inside a callback to select which blocks to change. String replacement is appropriate for
known markup; for general text edits, use an HTML parser to avoid modifying tag names or attributes accidentally.

Edits retain the original source markup and unrelated nodes, including wrapper fragments, unknown plugin blocks,
and whitespace. Unchanged blocks retain their source objects. Changed HTML may be normalized by jsoup.
If a callback or adapter throws, that edit operation leaves the document unchanged; earlier successful operations
in the chain remain applied. Callbacks should only modify the supplied editor, not the enclosing document.

Use `.className(...)` and `.anchor(...)` when creating blocks. Existing comment attributes must remain unchanged;
attempts to change them are rejected by the adapters. Dedicated heading levels and image IDs can be edited.
Set `.id((Long) null)` to remove an image ID and `.captionHtml(null)` to remove its caption.

Call `.build()` to obtain a `WpBlockDocument` with a shallow immutable copy of the top-level node list, or
`.serialize(true)` for pretty output. `.node(existingNode)` appends a generic block or literal HTML fragment,
so other block types can be mixed with fluent content. The facade is mutable and not thread-safe; built trees
are not deeply immutable. Codec and adapter APIs remain available for lower-level control.

Fluent HTML generation is currently limited to paragraphs, headings and images. Other block types still require
consistent saved markup; adding a generic node does not generate wrappers or render dynamic blocks.

## Parse and Serialize Content

Create a `DefaultWpGutenbergCodec` to parse a string into a `WpBlockDocument` and serialize it back to stored markup.
Codec instances can be shared between threads.

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.WpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;

WpGutenbergCodec codec = new DefaultWpGutenbergCodec();
WpBlockDocument document = codec.parse("""
        <!-- wp:paragraph -->
        <p>Hello <strong>world</strong>.</p>
        <!-- /wp:paragraph -->
        """);

for (WpContentNode node : document.nodes()) {
    if (node instanceof WpBlock block) {
        System.out.println(block.name()); // core/paragraph
    }
}

String rawContent = codec.serialize(document);
```

Serialization preserves literal HTML fragments and their order, but may normalize block comments and JSON attribute
formatting. Do not expect the complete serialized string to be byte-for-byte identical to the input.

`codec.serialize(document, true)` adds line breaks around block delimiters for readability. Those line breaks become
literal content when parsed again. Use the default overload, or `false`, when saving existing content without adding
whitespace.

## Understand the Document Model

| Type | What it represents |
|------|--------------------|
| `WpBlockDocument` | The ordered top-level content of a post or page, exposed by `nodes()`. |
| `WpBlock` | A block's fully qualified `name()`, JSON `attributes()`, ordered `content()`, and delimiter `syntax()`. |
| `WpHtmlFragment` | Literal HTML, text, ordinary comments, or whitespace, exposed by `html()`. |
| `WpBlockSyntax.PAIRED` | An opening and closing block comment surrounding content. |
| `WpBlockSyntax.SELF_CLOSING` | A single block comment with no inner content. |

Both blocks and HTML fragments implement `WpContentNode`. A block's `content()` can contain nested blocks interleaved
with HTML fragments. Preserve that order when editing: HTML fragments can contain the wrappers around child blocks.
Whitespace between top-level blocks is also a node, so a document's first node is not necessarily a block.

Core names are fully qualified in Java (`core/paragraph`) and serialized using the usual shorthand (`wp:paragraph`).
Plugin blocks retain their namespace, such as `my-plugin/card`. Unknown block types remain generic `WpBlock` instances;
no adapter is required to parse or serialize them. Content without Gutenberg delimiters remains literal HTML.

## Read, Edit, and Save a Post

Configure an authenticated `WpRestClient` as described in [Quick Start](../getting_started/quick-start.md) and
[Authentication](../getting_started/authentication.md). Request `WpContext.EDIT` with a user who can edit the post so
that the response includes raw content. Rendered content is unsuitable for preserving Gutenberg block structure.

The following method updates a known rich-text fragment in every paragraph, including paragraphs inside groups
and quotes. The facade preserves the rest of the document and the original block sources automatically.

```java
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.gutenberg.Gutenberg;

public static void updateParagraphs(WpRestClient restClient, long postId) {
    WpPost post = restClient.posts().get(postId, WpContext.EDIT);
    if (post.getContent() == null || post.getContent().getRaw() == null) {
        throw new IllegalStateException("The response does not contain content.raw");
    }

    var document = Gutenberg.parse(post.getContent().getRaw());
    String before = document.serialize();
    String updated = document
            .editParagraphs(p -> p.html(p.html().replace(
                    "<strong>world</strong>", "<strong>WordPress</strong>")))
            .serialize();
    if (updated.equals(before)) {
        return; // No matching fragment: nothing to save.
    }
    restClient.posts().update(postId, WpPostCreateUpdateRequest.builder()
            .withContent(updated)
            .build());
}
```

Updating `content` replaces the post's entire content string, so serialize the full edited document. For pages, use
`restClient.pages()`, `WpPageCreateUpdateRequest`, and the same codec workflow. See also
[Post Operations](../samples/post_operations.md) and [Page Operations](../samples/page_operations.md).

## Create New Blocks

Use the fluent API to generate supported blocks without looking up adapters:

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.Gutenberg;

String rawContent = Gutenberg.document()
        .heading("Introduction")
        .paragraph(p -> p.html("Hello <strong>world</strong>."))
        .serialize();
```

Pass `rawContent` to `.withContent(rawContent)` when building a post or page create/update request.

For plugin blocks or markup you already have, construct a generic block directly:

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockSyntax;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;

import java.util.List;
import java.util.Map;

WpBlock paragraph = new WpBlock(
        "core/paragraph",
        Map.of(),
        List.of(new WpHtmlFragment("<p>Hello world.</p>")),
        WpBlockSyntax.PAIRED);

WpBlock latestPosts = new WpBlock(
        "core/latest-posts",
        Map.of("postsToShow", 3),
        List.of(),
        WpBlockSyntax.SELF_CLOSING);
```

Self-closing blocks must have empty content. Their delimiter form does not by itself indicate whether WordPress renders
them dynamically. The codec only writes the stored markup; WordPress supplies any dynamic output.

## Typed Adapters and Supported Blocks

`DefaultWpBlockAdapterRegistry` provides the module's pinned catalog of 115 WordPress 7.1 block definitions. Look up
adapters by fully qualified block name or Java model type:

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpBlockAdapterRegistry;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;

DefaultWpBlockAdapterRegistry registry = new DefaultWpBlockAdapterRegistry();
WpBlockAdapter<WpParagraphBlock> adapter = registry
        .findByModelType(WpParagraphBlock.class)
        .orElseThrow();

System.out.println(registry.blockNames());
boolean supported = registry.findByBlockName("my-plugin/card").isPresent();
```

The catalog covers content, media, layout, interactive, navigation, site, post, query, comments, reusable blocks, and
widgets. A registered adapter does not necessarily provide rich editing or generate HTML from attributes:

- Specialized editing adapters, such as `ParagraphBlockAdapter`, expose supported content fields and can regenerate
  the corresponding markup. Unsupported edits may throw `IllegalArgumentException`.
- Models implementing `WpSavedBlockModel` carry attributes, saved content, and delimiter syntax. You are responsible
  for keeping attributes and saved HTML consistent when changing them.
- Unsupported blocks can always stay in the generic document tree and be serialized with the rest of the content.

To add custom conversions, implement `WpBlockAdapter<T>` and supply your adapters to
`new DefaultWpBlockAdapterRegistry(adapters)`. This constructor registers **only** the supplied collection; it does not
merge it with the default catalog. Duplicate block names or model types are rejected. Adapter conversion is explicit;
the codec does not automatically invoke the registry.

## Errors and Editing Limits

Malformed block delimiters, mismatched closing names, and invalid attribute JSON cause `WpBlockParseException`.
The parser is strict and does not repair malformed input. Use `getOffset()` to locate the offending delimiter; it is a
zero-based offset in Java UTF-16 code units.

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.WpBlockParseException;

try {
    new DefaultWpGutenbergCodec().parse("<!-- wp:paragraph -->");
} catch (WpBlockParseException exception) {
    System.err.println("Invalid block at offset " + exception.getOffset()
            + ": " + exception.getMessage());
}
```

Serialization rejects invalid block names, missing block structure, or a self-closing block with inner content using
`IllegalArgumentException`. Supply JSON-compatible attribute values, non-null nodes and collections, and an acyclic
content tree. Null parser input is rejected with `NullPointerException`.

!!! warning "Attributes and saved HTML must agree"
    Changing a generic block's attributes does not regenerate its saved HTML. The codec does not validate Gutenberg
    editor save output, sanitize HTML, or resolve referenced patterns and media. Use a supported editing adapter or
    supply consistent saved markup, then check the result in your site's block editor.

Documents and blocks are not deeply immutable. When using the lower-level API, copy collections before editing them. The fluent facade handles
node replacement for you. Do not mutate a document while it is being serialized.
