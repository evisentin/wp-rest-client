# Gutenberg content codec

Optional Java 21 module for parsing and serializing stored WordPress block markup.
It is independent of `wp-rest-client-domain` and both HTTP adapters. Runtime dependencies
are Jackson, jsoup and Apache Commons Lang; Lombok is used at compile time.

## Dependency

```xml

<dependency>
    <groupId>io.github.evisentin</groupId>
    <artifactId>wp-rest-client-gutenberg</artifactId>
    <version>1.4.9-SNAPSHOT</version>
</dependency>
```

Use the version matching the rest of your client modules.

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

## Parse and serialize

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;

var codec = new DefaultWpGutenbergCodec();
var document = codec.parse("<!-- wp:paragraph --><p>Hello</p><!-- /wp:paragraph -->");
String rawContent = codec.serialize(document);
```

When integrating with the REST client, fetch a post/page with authorized `context=edit`,
pass `post.getContent().getRaw()` to `parse`, and assign the serialized string to the
write request with `request.setContent(rawContent)`. Use `content.raw`, not `content.rendered`.

The document contains ordered literal HTML fragments and blocks, including unknown plugin
blocks. Nested blocks retain their positions within wrapper HTML. Core block names are
normalized to `core/name` in the model and shortened in stored delimiters. JSON attributes
are escaped for embedding in HTML comments. Literal HTML is preserved, while comment
spacing and JSON formatting may be normalized.

### Pretty printing

Enable readable output with the per-call flag:

```java
var codec = new DefaultWpGutenbergCodec();
System.out.println(codec.serialize(document, true)); // prettyPrint
```

For a paragraph, this produces:

```html
<!-- wp:paragraph -->
<p>Hello</p>
<!-- /wp:paragraph -->
```

Pretty mode inserts LF line breaks around block delimiters, including nested blocks, without
reformatting HTML fragments, inline elements or JSON attributes. Existing line breaks are
preserved, adjacent HTML fragments stay together, and no trailing newline is added.
The inserted whitespace becomes literal content when parsed again and can affect
whitespace-sensitive markup. Use `serialize(document)` or `serialize(document, false)`
when exact saved-HTML preservation matters. Parsing behavior is the same in either mode.

Malformed block comments, invalid attribute JSON, mismatched closers and unclosed blocks
raise `WpBlockParseException` with a zero-based UTF-16 offset. This is intentionally strict;
it does not reproduce WordPress's recovery of malformed documents. Empty and classic HTML
content are supported. Self-closing blocks with content are rejected during serialization.

The codec does not render dynamic blocks, validate editor save output, or regenerate HTML
when attributes change. The optional typed adapters described below operate separately from
the codec. Models must contain
JSON-compatible attribute values and an acyclic content tree, and must not be mutated
concurrently while being serialized.

Reference: [WordPress attribute serialization](https://developer.wordpress.org/reference/functions/serialize_block_attributes/).

## Block adapters (WordPress 7.1)

`DefaultWpBlockAdapterRegistry` registers all **115 block.json definitions** shipped in
[WordPress 7.1](https://wordpress.org/wordpress-7.1.tar.gz), including deprecated and internal
blocks. This is a pinned catalog, not the evolving Gutenberg plugin's experimental catalog.
Unknown plugin blocks remain usable through `WpBlock`. A registry can also be constructed
with an explicit collection of adapters for custom block types.

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpBlockAdapterRegistry;
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlockDocument;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.content.WpParagraphBlock;

import java.util.List;

var codec = new DefaultWpGutenbergCodec();
var document = codec.parse("<!-- wp:paragraph --><p>Hello</p><!-- /wp:paragraph -->");
var block = (WpBlock) document.nodes().getFirst();
var registry = new DefaultWpBlockAdapterRegistry();
var adapter = registry.findByModelType(WpParagraphBlock.class).orElseThrow();
var paragraph = adapter.fromBlock(block);
var updated = new WpParagraphBlock("<strong>Updated</strong>",
        paragraph.attributes(), paragraph.source());
WpBlock result = adapter.toBlock(updated);
String updatedContent = codec.serialize(new WpBlockDocument(List.of(result)));
```

Capabilities are deliberately explicit:

| Adapter                  | Behavior                                                                                                                                                                      |
|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ParagraphBlockAdapter`  | Extracts/edits rich-text HTML; supports new basic paragraphs with className, anchor, direction, dropCap and style.typography.textAlign.                                       |
| `HeadingBlockAdapter`    | Extracts/edits rich-text HTML and heading level; supports new basic headings with className, anchor and style.typography.textAlign.                                           |
| `ImageBlockAdapter`      | Extracts/edits media ID, URL, alt text and caption; preserves existing figure/link markup. New basic images support className, anchor, sizeSlug and linkDestination=none.     |
| `GroupBlockAdapter`      | Preserves the caller-supplied wrapper HTML and nested content in order. Does not generate layout wrappers.                                                                    |
| Other 111 named adapters | Convert to/from named saved-content records containing attributes, content and syntax. Preserve all saved markup and nesting; do not derive semantic fields or generate HTML. |

The example replaces the only block in a one-block document. For larger documents,
replace the selected node in its parent content list while retaining all other nodes,
including wrapper fragments and whitespace. Adapters do not update the document automatically.

Paragraph, heading and image records have an optional `source` component. Convenience constructors
still create new blocks; when editing an existing block, pass its source to preserve wrapper
attributes, links and unrecognized markup. Unchanged paragraph, heading and image conversions return the original source
block.
Edited HTML is parsed and serialized by jsoup and may have normalized quotes/entities.
Existing comment options must remain unchanged (except the dedicated heading level and image
ID fields). Unsupported new-block options are rejected rather than emitted with inconsistent
HTML. Rich-text HTML is trusted input, not sanitized content.

For the other named models, edit both stored attributes and saved HTML when they correspond.
For example, `WpListBlock` preserves `<ul>`/`<ol>` fragments and `core/list-item` children;
changing an `ordered` attribute alone does not rewrite the wrapper. Dynamic blocks retain their
self-closing form or saved fallback/inner content; rendering still happens in WordPress.
Legacy markers such as `<!--more-->` and `<!--nextpage-->` remain literal HTML in the codec;
the catalog adapters do not automatically reinterpret these as blocks.

The adapters do not execute JavaScript save functions, migrate deprecated markup, or reproduce
all theme-dependent block supports. The complete catalog denotes **saved-content model coverage**,
not full typed property access or HTML-generation support for every core block.

## Fluent model builders

Every record in `gutenberg.model` and its subpackages provides a Lombok `builder()`:

```java
var paragraph = WpParagraphBlock.builder()
        .contentHtml("Hello <strong>world</strong>")
        .build(); // attributes defaults to an empty map

var styledParagraph = WpParagraphBlock.builder()
        .contentHtml("Hello")
        .attribute("className", "intro")
        .attributes(Map.of("anchor", "welcome"))
        .build();
```

Import `model.content.WpParagraphBlock` under the module's package and `java.util.Map`
for these examples. Map components use `@Singular("attribute")`: `attribute(key, value)`
adds one entry, `attributes(map)` adds entries, and `clearAttributes()` removes them.
Omitted maps default to empty maps. Built maps are immutable shallow copies and retain
null values; explicitly passing a null map to `attributes(null)` is rejected.
List components also use `@Singular`: add individual children with `contentNode(node)` or
`node(node)`, supply collections with `content(collection)` or `nodes(collection)`, and reset
with `clearContent()` or `clearNodes()`. Omitted lists default to empty lists; built lists are
immutable shallow copies that preserve insertion order. Explicitly null collections are rejected.

```java
var block = WpBlock.builder()
        .name("core/paragraph")
        .contentNode(WpHtmlFragment.builder().html("<p>Hello</p>").build())
        .syntax(WpBlockSyntax.PAIRED)
        .build();
var document = WpBlockDocument.builder().node(block).build();
```

These types are in `io.github.evisentin.wordpress.rest.client.gutenberg.model`.
Noncollection components keep Lombok's normal defaults (`null` for references and `0` for integers),
so supply the fields required by the codec or adapter. Existing record constructors retain
their behavior, including retaining supplied collection references.

## Package structure

All packages below are relative to `io.github.evisentin.wordpress.rest.client.gutenberg`.

| Package                                    | Responsibility                                                                                                                                                                                |
|--------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Root package                               | Fluent `Gutenberg` facade; public codec, parser, serializer and registry contracts; default implementations and parse exception. `GutenbergParser` and `GutenbergSerializer` are package-private implementation classes. |
| `model`                                    | Generic document tree: `WpBlockDocument`, `WpBlock`, `WpContentNode`, `WpHtmlFragment`, delimiter syntax and `WpMissingBlock`.                                                                |
| `adapters`                                 | `WpBlockAdapter`, shared markup helpers and `MissingBlockAdapter`.                                                                                                                            |
| `model.<category>` / `adapters.<category>` | Matching named models and adapters grouped by content, media, layout, interactive, navigation, comments, post, query, site, reusable and widgets.                                             |
| `model.reusable` / `adapters.reusable`     | Pattern and synced-pattern types, plus the cross-category `WpSavedBlockModel` contract and `SavedBlockAdapter` base class.                                                                    |

These categories organize this Java API; they do not define WordPress editor categories.
Registry lookups use exact, fully qualified block names (for example `core/paragraph`)
or exact model classes. The collection constructor replaces the default catalog with
exactly the supplied adapters and rejects duplicate block names or model types.

Direct record construction retains supplied collection references; records are not deeply
immutable. Builders copy maps and lists as described above. Parser-created
content lists are unmodifiable, while parsed attribute maps may be mutable. Adapters make
shallow copies of attribute maps and, for saved-content models and groups, content lists.
Nested attribute values and child blocks remain shared. Keep trees acyclic and avoid concurrent
mutation when serializing or adapting them.

## Build and verification

Run from the repository root with JDK 21:

```sh
./mvnw -pl wp-rest-client-gutenberg clean verify -Dmaven.test.skip=false
./mvnw -pl wp-rest-client-gutenberg javadoc:javadoc -Dmaven.javadoc.failOnError=true
```

The `verify` phase generates the JaCoCo coverage report at `target/site/jacoco/index.html`.
The repository skips tests by default, so pass `-Dmaven.test.skip=false` to run them. For a coverage-only build without fetching external Javadoc
links, add `-Dmaven.javadoc.skip=true`.

Tests cover strict parsing, serialization, editing behavior, the pinned 115-name catalog,
and stored-markup fixtures for every registered adapter. The fixtures are illustrative;
they do not constitute validation by the WordPress editor.
