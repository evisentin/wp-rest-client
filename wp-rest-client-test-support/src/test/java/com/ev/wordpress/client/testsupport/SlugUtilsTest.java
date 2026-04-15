package com.ev.wordpress.client.testsupport;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class SlugUtilsTest implements WithAssertions {
    @Test
    void shouldConvertSimpleTextToSlug() {
        assertThat(SlugUtils.toWordPressSlug("Hello World"))
                .isEqualTo("hello-world");
    }

    @Test
    void shouldHandleComplexCase() {
        assertThat(SlugUtils.toWordPressSlug("  Héllo---Wörld's!!!  "))
                .isEqualTo("hello-worlds");
    }

    @Test
    void shouldRemoveApostrophes() {
        assertThat(SlugUtils.toWordPressSlug("John's Post"))
                .isEqualTo("johns-post");

        assertThat(SlugUtils.toWordPressSlug("John’s Post"))
                .isEqualTo("johns-post");
    }

    @Test
    void shouldRemoveDiacritics() {
        assertThat(SlugUtils.toWordPressSlug("Café del Mar"))
                .isEqualTo("cafe-del-mar");
    }

    @Test
    void shouldRemoveLeadingAndTrailingDashes() {
        assertThat(SlugUtils.toWordPressSlug("!!!Hello World!!!"))
                .isEqualTo("hello-world");
    }

    @Test
    void shouldReplaceNonAlphanumericWithDash() {
        assertThat(SlugUtils.toWordPressSlug("Hello---World!!!"))
                .isEqualTo("hello-world");
    }

    @Test
    void shouldReturnEmptyStringWhenNoValidCharacters() {
        assertThat(SlugUtils.toWordPressSlug("!!!"))
                .isEqualTo("");
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertThat(SlugUtils.toWordPressSlug(null)).isNull();
    }

    @Test
    void shouldTrimAndLowercase() {
        assertThat(SlugUtils.toWordPressSlug("  Foo Bar  "))
                .isEqualTo("foo-bar");
    }
}
