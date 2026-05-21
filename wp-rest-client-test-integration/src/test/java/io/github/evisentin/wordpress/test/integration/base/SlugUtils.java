package io.github.evisentin.wordpress.test.integration.base;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.Locale;

/**
 * Utility class for generating WordPress-compatible slugs from arbitrary strings.
 * <p>
 * This class is intended for use in unit tests where predictable and normalized slug values are required. It mimics
 * common WordPress slug generation behaviour by applying the following transformations:
 * <ul>
 *     <li>Trims leading and trailing whitespace</li>
 *     <li>Converts all characters to lowercase using {@link Locale#ROOT}</li>
 *     <li>Removes diacritics (e.g., "é" → "e", "ü" → "u")</li>
 *     <li>Removes apostrophes (both ASCII and typographic)</li>
 *     <li>Replaces sequences of non-alphanumeric characters with a single dash ({@code -})</li>
 *     <li>Removes leading and trailing dashes</li>
 * </ul>
 * <p>
 * This class cannot be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SlugUtils {

    /**
     * Converts the given input string into a WordPress-style slug.
     *
     * <p>Examples:
     * <pre>
     * "Hello World!"      → "hello-world"
     * "Café del Mar"     → "cafe-del-mar"
     * "  Foo---Bar  "    → "foo-bar"
     * "John's Post"      → "johns-post"
     * </pre>
     *
     * @param input
     *         the input string to convert; may be {@code null}
     *
     * @return a normalized slug string, or {@code null} if the input is {@code null}
     */
    public static String toWordPressSlug(final String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }

        String slug = input.trim().toLowerCase(Locale.ROOT);

        // Remove accents/diacritics: é -> e, ü -> u
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");

        // Replace apostrophes entirely
        slug = slug.replace("'", "").replace("’", "");

        // Replace any run of non-alphanumeric chars with a dash
        slug = slug.replaceAll("[^a-z0-9]+", "-");

        // Remove leading/trailing dashes
        slug = slug.replaceAll("^-+|-+$", "");

        return slug;
    }
}
