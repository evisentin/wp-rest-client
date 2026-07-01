package io.github.evisentin.wordpress.rest.client.domain.assertions;

import io.github.evisentin.wordpress.rest.client.domain.model.WpComment;
import io.github.evisentin.wordpress.rest.client.domain.model.enums.WpCommentStatus;
import org.assertj.core.api.AbstractObjectAssert;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractWpCommentAssert<S extends AbstractWpCommentAssert<S>>
        extends AbstractObjectAssert<S, WpComment> {

    protected AbstractWpCommentAssert(final WpComment actual, final Class<?> selfType) {
        super(actual, selfType);
    }

    public S hasAuthorAvatarUrls(final Map<String, String> expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorAvatarUrls(), expected)) {
            failWithMessage("Expected comment authorAvatarUrls to be <%s> but was <%s>",
                    expected, actual.getAuthorAvatarUrls());
        }
        return myself;
    }

    public S hasAuthorId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorId(), expected)) {
            failWithMessage("Expected comment authorId to be <%s> but was <%s>", expected, actual.getAuthorId());
        }
        return myself;
    }

    public S hasAuthorName(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorName(), expected)) {
            failWithMessage("Expected comment authorName to be <%s> but was <%s>", expected, actual.getAuthorName());
        }
        return myself;
    }

    public S hasAuthorUrl(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getAuthorUrl(), expected)) {
            failWithMessage("Expected comment authorUrl to be <%s> but was <%s>", expected, actual.getAuthorUrl());
        }
        return myself;
    }

    public S hasContentSatisfying(final Consumer<WpRenderedFieldAssert> requirements) {
        isNotNull();
        if (actual.getContent() == null) {
            failWithMessage("Expected comment content to be present but it was null");
        }
        requirements.accept(new WpRenderedFieldAssert(actual.getContent()));
        return myself;
    }

    public S hasDate(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDate(), expected)) {
            failWithMessage("Expected comment date to be <%s> but was <%s>", expected, actual.getDate());
        }
        return myself;
    }

    public S hasDateGMT(final LocalDateTime expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getDateGMT(), expected)) {
            failWithMessage("Expected comment dateGMT to be <%s> but was <%s>", expected, actual.getDateGMT());
        }
        return myself;
    }

    public S hasId() {
        isNotNull();

        if (actual.getId() == null) {
            failWithMessage("Expected comment id to be non-null");
        }

        return myself;
    }

    public S hasId(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getId(), expected)) {
            failWithMessage("Expected comment id to be <%s> but was <%s>", expected, actual.getId());
        }
        return myself;
    }

    public S hasLink(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getLink(), expected)) {
            failWithMessage("Expected comment link to be <%s> but was <%s>", expected, actual.getLink());
        }
        return myself;
    }

    public S hasParent(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getParent(), expected)) {
            failWithMessage("Expected comment parent to be <%s> but was <%s>", expected, actual.getParent());
        }
        return myself;
    }

    public S hasPost(final Long expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getPost(), expected)) {
            failWithMessage("Expected comment post to be <%s> but was <%s>", expected, actual.getPost());
        }
        return myself;
    }

    public S hasStatus(final WpCommentStatus expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getStatus(), expected)) {
            failWithMessage("Expected comment status to be <%s> but was <%s>", expected, actual.getStatus());
        }
        return myself;
    }

    public S hasType(final String expected) {
        isNotNull();
        if (!java.util.Objects.equals(actual.getType(), expected)) {
            failWithMessage("Expected comment type to be <%s> but was <%s>", expected, actual.getType());
        }
        return myself;
    }
}
