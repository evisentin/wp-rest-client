package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpBlock;
import io.github.evisentin.wordpress.rest.client.gutenberg.model.WpHtmlFragment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class AdapterTestSupport {
    private AdapterTestSupport() {}

    public static WpBlock block(String raw) {
        return (WpBlock) new DefaultWpGutenbergCodec().parse(raw).nodes().getFirst();
    }

    public static Document html(WpBlock block) {
        String savedHtml = block.content().stream().map(WpHtmlFragment.class::cast)
                                .map(WpHtmlFragment::html).collect(java.util.stream.Collectors.joining());
        return Jsoup.parseBodyFragment(savedHtml);
    }
}
