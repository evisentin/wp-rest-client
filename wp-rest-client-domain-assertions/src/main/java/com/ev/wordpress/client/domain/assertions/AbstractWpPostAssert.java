package com.ev.wordpress.client.domain.assertions;

import com.ev.wordpress.client.domain.dto.WpPost;
import com.ev.wordpress.client.domain.dto.enums.WpOpenClosed;
import com.ev.wordpress.client.domain.dto.enums.WpPostFormat;
import com.ev.wordpress.client.domain.dto.enums.WpPostStatus;
import org.assertj.core.api.AbstractObjectAssert;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractWpPostAssert<SELF extends AbstractWpPostAssert<SELF>>
        extends AbstractObjectAssert<SELF, WpPost> {

    protected AbstractWpPostAssert(final WpPost actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public SELF hasAuthorId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorId(), expected)) {
            failWithMessage("Expected authorId to be <%s> but was <%s>", expected, actual.getAuthorId());
        }
        return myself;
    }

    public SELF hasCategories(final Set<Long> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getCategories(), expected)) {
            failWithMessage("Expected categories to be <%s> but was <%s>", expected, actual.getCategories());
        }
        return myself;
    }

    public SELF hasCommentStatus(final WpOpenClosed expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getCommentStatus(), expected)) {
            failWithMessage("Expected commentStatus to be <%s> but was <%s>", expected, actual.getCommentStatus());
        }
        return myself;
    }

    public SELF hasContentSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getContent() == null) {
            failWithMessage("Expected content to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getContent()));
        return myself;
    }

    public SELF hasDate(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDate(), expected)) {
            failWithMessage("Expected post date to be <%s> but was <%s>", expected, actual.getDate());
        }
        return myself;
    }

    public SELF hasDateGMT(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDateGMT(), expected)) {
            failWithMessage("Expected post dateGMT to be <%s> but was <%s>", expected, actual.getDateGMT());
        }
        return myself;
    }

    public SELF hasExcerptSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getExcerpt() == null) {
            failWithMessage("Expected excerpt to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getExcerpt()));
        return myself;
    }

    public SELF hasFeaturedMediaId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getFeaturedMediaId(), expected)) {
            failWithMessage("Expected featuredMediaId to be <%s> but was <%s>", expected, actual.getFeaturedMediaId());
        }
        return myself;
    }

    public SELF hasFormat(final WpPostFormat expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getFormat(), expected)) {
            failWithMessage("Expected format to be <%s> but was <%s>", expected, actual.getFormat());
        }
        return myself;
    }

    public SELF hasGeneratedSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getGeneratedSlug(), expected)) {
            failWithMessage("Expected generatedSlug to be <%s> but was <%s>", expected, actual.getGeneratedSlug());
        }
        return myself;
    }

    public SELF hasGuidSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getGuid() == null) {
            failWithMessage("Expected guid to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getGuid()));
        return myself;
    }

    public SELF hasId() {
        isNotNull();

        if (actual.getId() == null) {
            failWithMessage("Expected post id to be non-null");
        }

        return myself;
    }

    public SELF hasId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getId(), expected)) {
            failWithMessage("Expected post id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public SELF hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected post link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public SELF hasModified(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getModified(), expected)) {
            failWithMessage("Expected post modified to be <%s> but was <%s>", expected, actual.getModified());
        }
        return myself;
    }

    public SELF hasModifiedGMT(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getModifiedGMT(), expected)) {
            failWithMessage("Expected post modifiedGMT to be <%s> but was <%s>", expected, actual.getModifiedGMT());
        }
        return myself;
    }

    public SELF hasPassword(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPassword(), expected)) {
            failWithMessage("Expected post password to be <%s> but was <%s>", expected, actual.getPassword());
        }
        return myself;
    }

    public SELF hasPermalinkTemplate(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPermalinkTemplate(), expected)) {
            failWithMessage("Expected permalinkTemplate to be <%s> but was <%s>", expected, actual.getPermalinkTemplate());
        }
        return myself;
    }

    public SELF hasPingStatus(final WpOpenClosed expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPingStatus(), expected)) {
            failWithMessage("Expected pingStatus to be <%s> but was <%s>", expected, actual.getPingStatus());
        }
        return myself;
    }

    public SELF hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected post slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public SELF hasStatus(final WpPostStatus expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getStatus(), expected)) {
            failWithMessage("Expected post status to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }

    public SELF hasTags(final Set<Long> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTags(), expected)) {
            failWithMessage("Expected tags to be <%s> but was <%s>", expected, actual.getTags());
        }
        return myself;
    }

    public SELF hasTemplate(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTemplate(), expected)) {
            failWithMessage("Expected template to be <%s> but was <%s>", expected, actual.getTemplate());
        }
        return myself;
    }

    public SELF hasTitleSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getTitle() == null) {
            failWithMessage("Expected title to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getTitle()));
        return myself;
    }

    public SELF hasType(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getType(), expected)) {
            failWithMessage("Expected post type to be <%s> but was <%s>", expected, actual.getType());
        }
        return myself;
    }

    public SELF isNotSticky() {
        isNotNull();
        if (actual.isSticky()) {
            failWithMessage("Expected post not to be sticky but it was");
        }
        return myself;
    }

    public SELF isSticky() {
        isNotNull();
        if (!actual.isSticky()) {
            failWithMessage("Expected post to be sticky but it was not");
        }
        return myself;
    }
}
