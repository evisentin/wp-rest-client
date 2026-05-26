package io.github.evisentin.wordpress.client.domain.assertions;

import io.github.evisentin.wordpress.client.domain.model.WpMedia;
import io.github.evisentin.wordpress.client.domain.model.enums.WpMediaStatus;
import io.github.evisentin.wordpress.client.domain.model.enums.WpOpenClosed;
import io.github.evisentin.wordpress.client.domain.model.enums.WpPostStatus;
import org.assertj.core.api.AbstractObjectAssert;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public abstract class AbstractWpMediaAssert<S extends AbstractWpMediaAssert<S>>
        extends AbstractObjectAssert<S, WpMedia> {

    protected AbstractWpMediaAssert(final WpMedia actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasAuthorId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorId(), expected)) {
            failWithMessage("Expected authorId to be <%s> but was <%s>", expected, actual.getAuthorId());
        }
        return myself;
    }

    public S hasCommentStatus(final WpOpenClosed expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getCommentStatus(), expected)) {
            failWithMessage("Expected commentStatus to be <%s> but was <%s>", expected, actual.getCommentStatus());
        }
        return myself;
    }

    public S hasDate(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDate(), expected)) {
            failWithMessage("Expected madia date to be <%s> but was <%s>", expected, actual.getDate());
        }
        return myself;
    }

    public S hasDateGMT(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDateGMT(), expected)) {
            failWithMessage("Expected madia dateGMT to be <%s> but was <%s>", expected, actual.getDateGMT());
        }
        return myself;
    }

    public S hasExcerptSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getExcerpt() == null) {
            failWithMessage("Expected excerpt to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getExcerpt()));
        return myself;
    }

    public S hasFeaturedMediaId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getFeaturedMediaId(), expected)) {
            failWithMessage("Expected featuredMediaId to be <%s> but was <%s>", expected, actual.getFeaturedMediaId());
        }
        return myself;
    }

    public S hasGeneratedSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getGeneratedSlug(), expected)) {
            failWithMessage("Expected generatedSlug to be <%s> but was <%s>", expected, actual.getGeneratedSlug());
        }
        return myself;
    }

    public S hasGuidSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getGuid() == null) {
            failWithMessage("Expected guid to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getGuid()));
        return myself;
    }

    public S hasId() {
        isNotNull();

        if (actual.getId() == null) {
            failWithMessage("Expected madia id to be non-null");
        }

        return myself;
    }

    public S hasId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getId(), expected)) {
            failWithMessage("Expected madia id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public S hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected madia link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public S hasModified(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getModified(), expected)) {
            failWithMessage("Expected madia modified to be <%s> but was <%s>", expected, actual.getModified());
        }
        return myself;
    }

    public S hasModifiedGMT(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getModifiedGMT(), expected)) {
            failWithMessage("Expected madia modifiedGMT to be <%s> but was <%s>", expected, actual.getModifiedGMT());
        }
        return myself;
    }

    public S hasPermalinkTemplate(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPermalinkTemplate(), expected)) {
            failWithMessage("Expected permalinkTemplate to be <%s> but was <%s>", expected, actual.getPermalinkTemplate());
        }
        return myself;
    }

    public S hasPingStatus(final WpOpenClosed expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPingStatus(), expected)) {
            failWithMessage("Expected pingStatus to be <%s> but was <%s>", expected, actual.getPingStatus());
        }
        return myself;
    }

    public S hasSlug(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getSlug(), expected)) {
            failWithMessage("Expected madia slug to be <%s> but was <%s>", expected, actual.getSlug());
        }
        return myself;
    }

    public S hasStatus(final WpMediaStatus expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getStatus(), expected)) {
            failWithMessage("Expected media status to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }
    public S hasMediaType(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getMediaType(), expected)) {
            failWithMessage("Expected media type to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }

    public S hasMimeType(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getMimeType(), expected)) {
            failWithMessage("Expected media mime type to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }

    public S hasTemplate(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getTemplate(), expected)) {
            failWithMessage("Expected template to be <%s> but was <%s>", expected, actual.getTemplate());
        }
        return myself;
    }

    public S hasTitleSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getTitle() == null) {
            failWithMessage("Expected title to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getTitle()));
        return myself;
    }

    public S hasType(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getType(), expected)) {
            failWithMessage("Expected madia type to be <%s> but was <%s>", expected, actual.getType());
        }
        return myself;
    }
}
