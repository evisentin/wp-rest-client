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

The following method reads the first top-level paragraph and replaces only `<strong>world</strong>` with
`<strong>WordPress</strong>` inside its existing content. For example:

```html title="Before"
<p>Hello <strong>world</strong>. This sentence stays the same.</p>
```

```html title="After"
<p>Hello <strong>WordPress</strong>. This sentence stays the same.</p>
```

The surrounding text, inline formatting, and other document nodes are retained. The method returns without sending
an update if there is no top-level paragraph or the first paragraph does not contain the target fragment. Nested
paragraphs require traversing the enclosing block's `content()` as well.

```java
import io.github.evisentin.wordpress.rest.client.domain.WpRestClient;
import io.github.evisentin.wordpress.rest.client.domain.model.WpPost;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpContext;
import io.github.evisentin.wordpress.rest.client.domain.model.requests.WpPostCreateUpdateRequest;
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.WpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.ParagraphBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpContentNode;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;

import java.util.ArrayList;
import java.util.List;

public static void updateFirstParagraph(WpRestClient restClient, long postId) {
    WpPost post = restClient.posts().get(postId, WpContext.EDIT);
    if (post.getContent() == null || post.getContent().getRaw() == null) {
        throw new IllegalStateException("The response does not contain content.raw");
    }

    WpGutenbergCodec codec = new DefaultWpGutenbergCodec();
    WpBlockDocument document = codec.parse(post.getContent().getRaw());
    List<WpContentNode> nodes = new ArrayList<>(document.nodes());
    ParagraphBlockAdapter adapter = new ParagraphBlockAdapter();

    for (int i = 0; i < nodes.size(); i++) {
        if (nodes.get(i) instanceof WpBlock block
                && block.name().equals(adapter.blockName())) {
            WpParagraphBlock original = adapter.fromBlock(block);
            String updatedHtml = original.contentHtml().replace(
                    "<strong>world</strong>", "<strong>WordPress</strong>");
            if (updatedHtml.equals(original.contentHtml())) {
                return; // No matching fragment: nothing to save.
            }

            WpParagraphBlock edited = new WpParagraphBlock(
                    updatedHtml,
                    original.attributes(),
                    original.source());
            nodes.set(i, adapter.toBlock(edited));

            String rawContent = codec.serialize(new WpBlockDocument(nodes));
            restClient.posts().update(postId, WpPostCreateUpdateRequest.builder()
                    .withContent(rawContent)
                    .build());
            return;
        }
    }
}
```

Keep `source()` when editing an existing paragraph so the adapter can preserve its wrapper markup. This adapter
supports changing rich-text content while retaining the existing attributes; changing those attributes is rejected.
`contentHtml()` contains the paragraph's inner HTML, without the outer `<p>` element. Start from that value and
pass the modified HTML to the new model to retain the rest of the paragraph. Here, `String.replace` replaces every
exact occurrence of the known HTML fragment. It is suitable for this controlled markup; for general text edits,
use an HTML parser to modify text nodes so that tag names and attributes are not accidentally changed.

Updating `content` replaces the post's entire content string, so serialize the full edited document. For pages, use
`restClient.pages()`, `WpPageCreateUpdateRequest`, and the same codec workflow. See also
[Post Operations](../samples/post_operations.md) and [Page Operations](../samples/page_operations.md).

## Create New Blocks

Use a typed adapter to generate markup for supported editing operations. For example, create a paragraph from rich-text
HTML and wrap it in a document:

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.ParagraphBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;

import java.util.List;
import java.util.Map;

ParagraphBlockAdapter adapter = new ParagraphBlockAdapter();
WpBlock paragraph = adapter.toBlock(
        new WpParagraphBlock("Hello <strong>world</strong>.", Map.of()));
String rawContent = new DefaultWpGutenbergCodec().serialize(
        new WpBlockDocument(List.of(paragraph)));
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

Documents and blocks are not deeply immutable. Copy collections when making edits, as in the post example, and do not
mutate a document while it is being serialized.
