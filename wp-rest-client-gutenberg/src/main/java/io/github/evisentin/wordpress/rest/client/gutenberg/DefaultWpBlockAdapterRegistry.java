package io.github.evisentin.wordpress.rest.client.gutenberg;

import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.MissingBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.WpBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.comments.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.content.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.interactive.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.layout.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.media.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.navigation.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.post.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.query.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.PatternBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.reusable.SyncedPatternBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.site.*;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.widgets.LegacyWidgetBlockAdapter;
import io.github.evisentin.wordpress.rest.client.gutenberg.adapters.widgets.WidgetGroupBlockAdapter;

import java.util.*;

/**
 * Immutable lookup maps for typed block adapters. The default constructor registers the pinned WordPress 7.1 catalog of
 * 115 block definitions; the collection constructor registers only its supplied adapters. Adapter instances are
 * retained, so custom adapters determine their own thread safety.
 */
public final class DefaultWpBlockAdapterRegistry implements WpBlockAdapterRegistry {
    private final Map<String, WpBlockAdapter<?>> byName;
    private final Map<Class<?>, WpBlockAdapter<?>> byType;

    /**
     * Registers the WordPress 7.1 core adapters.
     */
    public DefaultWpBlockAdapterRegistry() {
        this(List.of(
                new AccordionBlockAdapter(),
                new AccordionHeadingBlockAdapter(),
                new AccordionItemBlockAdapter(),
                new AccordionPanelBlockAdapter(),
                new ArchivesBlockAdapter(),
                new AudioBlockAdapter(),
                new AvatarBlockAdapter(),
                new SyncedPatternBlockAdapter(),
                new BreadcrumbsBlockAdapter(),
                new ButtonBlockAdapter(),
                new ButtonsBlockAdapter(),
                new CalendarBlockAdapter(),
                new CategoriesBlockAdapter(),
                new CodeBlockAdapter(),
                new ColumnBlockAdapter(),
                new ColumnsBlockAdapter(),
                new CommentAuthorNameBlockAdapter(),
                new CommentContentBlockAdapter(),
                new CommentDateBlockAdapter(),
                new CommentEditLinkBlockAdapter(),
                new CommentReplyLinkBlockAdapter(),
                new CommentTemplateBlockAdapter(),
                new CommentsBlockAdapter(),
                new CommentsPaginationBlockAdapter(),
                new CommentsPaginationNextBlockAdapter(),
                new CommentsPaginationNumbersBlockAdapter(),
                new CommentsPaginationPreviousBlockAdapter(),
                new CommentsTitleBlockAdapter(),
                new CoverBlockAdapter(),
                new DetailsBlockAdapter(),
                new EmbedBlockAdapter(),
                new FileBlockAdapter(),
                new FootnotesBlockAdapter(),
                new FreeformBlockAdapter(),
                new GalleryBlockAdapter(),
                new GroupBlockAdapter(),
                new HeadingBlockAdapter(),
                new HomeLinkBlockAdapter(),
                new HtmlBlockAdapter(),
                new IconBlockAdapter(),
                new ImageBlockAdapter(),
                new LatestCommentsBlockAdapter(),
                new LatestPostsBlockAdapter(),
                new LegacyWidgetBlockAdapter(),
                new ListBlockAdapter(),
                new ListItemBlockAdapter(),
                new LoginoutBlockAdapter(),
                new MathBlockAdapter(),
                new MediaTextBlockAdapter(),
                new MissingBlockAdapter(),
                new MoreBlockAdapter(),
                new NavigationBlockAdapter(),
                new NavigationLinkBlockAdapter(),
                new NavigationOverlayCloseBlockAdapter(),
                new NavigationSubmenuBlockAdapter(),
                new NextpageBlockAdapter(),
                new PageListBlockAdapter(),
                new PageListItemBlockAdapter(),
                new ParagraphBlockAdapter(),
                new PatternBlockAdapter(),
                new PlaylistBlockAdapter(),
                new PlaylistTrackBlockAdapter(),
                new PostAuthorBlockAdapter(),
                new PostAuthorBiographyBlockAdapter(),
                new PostAuthorNameBlockAdapter(),
                new PostCommentsCountBlockAdapter(),
                new PostCommentsFormBlockAdapter(),
                new PostCommentsLinkBlockAdapter(),
                new PostContentBlockAdapter(),
                new PostDateBlockAdapter(),
                new PostExcerptBlockAdapter(),
                new PostFeaturedImageBlockAdapter(),
                new PostNavigationLinkBlockAdapter(),
                new PostTemplateBlockAdapter(),
                new PostTermsBlockAdapter(),
                new PostTimeToReadBlockAdapter(),
                new PostTitleBlockAdapter(),
                new PreformattedBlockAdapter(),
                new PullquoteBlockAdapter(),
                new QueryBlockAdapter(),
                new QueryNoResultsBlockAdapter(),
                new QueryPaginationBlockAdapter(),
                new QueryPaginationNextBlockAdapter(),
                new QueryPaginationNumbersBlockAdapter(),
                new QueryPaginationPreviousBlockAdapter(),
                new QueryTitleBlockAdapter(),
                new QueryTotalBlockAdapter(),
                new QuoteBlockAdapter(),
                new ReadMoreBlockAdapter(),
                new RssBlockAdapter(),
                new SearchBlockAdapter(),
                new SeparatorBlockAdapter(),
                new ShortcodeBlockAdapter(),
                new SiteLogoBlockAdapter(),
                new SiteTaglineBlockAdapter(),
                new SiteTitleBlockAdapter(),
                new SocialLinkBlockAdapter(),
                new SocialLinksBlockAdapter(),
                new SpacerBlockAdapter(),
                new TabListBlockAdapter(),
                new TabPanelBlockAdapter(),
                new TabPanelsBlockAdapter(),
                new TableBlockAdapter(),
                new TabsBlockAdapter(),
                new TagCloudBlockAdapter(),
                new TemplatePartBlockAdapter(),
                new TermCountBlockAdapter(),
                new TermDescriptionBlockAdapter(),
                new TermNameBlockAdapter(),
                new TermTemplateBlockAdapter(),
                new TermsQueryBlockAdapter(),
                new TextColumnsBlockAdapter(),
                new VerseBlockAdapter(),
                new VideoBlockAdapter(),
                new WidgetGroupBlockAdapter()));
    }

    /**
     * Creates a registry with explicitly selected core or custom adapters. Duplicate block names and model types are
     * rejected.
     *
     * @param adapters
     *         adapters to register
     */
    public DefaultWpBlockAdapterRegistry(Collection<? extends WpBlockAdapter<?>> adapters) {
        Map<String, WpBlockAdapter<?>> names = new LinkedHashMap<>();
        Map<Class<?>, WpBlockAdapter<?>> types = new LinkedHashMap<>();
        for (WpBlockAdapter<?> adapter : adapters) {
            Objects.requireNonNull(adapter, "adapter");
            String name = Objects.requireNonNull(adapter.blockName(), "blockName");
            Class<?> type = Objects.requireNonNull(adapter.modelType(), "modelType");
            if (names.putIfAbsent(name, adapter) != null || types.putIfAbsent(type, adapter) != null) {
                throw new IllegalArgumentException("Duplicate adapter name or model type: " + name);
            }
        }
        byName = Map.copyOf(names);
        byType = Map.copyOf(types);
    }

    /**
     * @return all registered block names
     */
    public Set<String> blockNames() {
        return byName.keySet();
    }

    @Override
    public Optional<WpBlockAdapter<?>> findByBlockName(String blockName) {
        return Optional.ofNullable(byName.get(blockName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<WpBlockAdapter<T>> findByModelType(Class<T> modelType) {
        // Construction associates each adapter only with its declared model class.
        return Optional.ofNullable((WpBlockAdapter<T>) byType.get(modelType));
    }
}
