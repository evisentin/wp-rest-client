# Gutenberg content codec

Optional module for parsing and serializing stored WordPress block markup. It depends on
`wp-rest-client-domain`, Jackson and jsoup; neither HTTP adapter needs this module.

```java
import io.github.evisentin.wordpress.rest.client.gutenberg.DefaultWpGutenbergCodec;

var codec = new DefaultWpGutenbergCodec();
// Fetch the post/page with authorized context=edit so content.raw is available.
var document = codec.parse(post.getContent().getRaw());
request.setContent(codec.serialize(document));
```

The document contains ordered literal HTML fragments and blocks, including unknown plugin
blocks. Nested blocks retain their positions within wrapper HTML. Core block names are
normalized to `core/name` in the model and shortened in stored delimiters. JSON attributes
are escaped for embedding in HTML comments. Literal HTML is preserved, while comment
spacing and JSON formatting may be normalized.

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
var registry = new DefaultWpBlockAdapterRegistry();
var adapter = registry.findByModelType(WpParagraphBlock.class).orElseThrow();
var paragraph = adapter.fromBlock(block);
var updated = new WpParagraphBlock("<strong>Updated</strong>",
        paragraph.attributes(), paragraph.source());
WpBlock result = adapter.toBlock(updated);
```

Capabilities are deliberately explicit:

| Adapter                  | Behavior                                                                                                                                                                      |
|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ParagraphBlockAdapter`  | Extracts/edits rich-text HTML; supports new basic paragraphs with className, anchor, direction, dropCap and style.typography.textAlign.                                       |
| `HeadingBlockAdapter`    | Extracts/edits rich-text HTML and heading level; supports new basic headings with className, anchor and style.typography.textAlign.                                           |
| `ImageBlockAdapter`      | Extracts/edits media ID, URL, alt text and caption; preserves existing figure/link markup. New basic images support className, anchor, sizeSlug and linkDestination=none.     |
| `GroupBlockAdapter`      | Preserves the caller-supplied wrapper HTML and nested content in order. Does not generate layout wrappers.                                                                    |
| Other 111 named adapters | Convert to/from named saved-content records containing attributes, content and syntax. Preserve all saved markup and nesting; do not derive semantic fields or generate HTML. |

Paragraph, heading and image records have an optional `source` component. Existing constructors
still create new blocks; when editing an existing block, pass its source to preserve wrapper
attributes, links and unrecognized markup. Unchanged conversions return the original block.
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
