package com.ev.wordpress.client.adapter.okhttp.query.mappers;

import lombok.NonNull;
import okhttp3.HttpUrl;
import org.assertj.core.api.WithAssertions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ParamMapperTest extends WithAssertions {

    default void assertThatUrlContainsExactly(final @NonNull HttpUrl url,
                                              final @NonNull Map<String, ?> parameters) {
        assertThat(url.queryParameterNames())
                .containsExactlyInAnyOrderElementsOf(parameters.keySet());

        parameters.forEach((key, value) -> {
            final List<String> actualValues = url.queryParameterValues(key);
            final List<String> expectedValues = toExpectedValues(value);

            assertThat(actualValues)
                    .withFailMessage(
                            "Expected query param '%s' to contain exactly %s but was %s",
                            key, expectedValues, actualValues
                    )
                    .containsExactlyElementsOf(expectedValues);
        });
    }

    default void assertThatUrlContainsNoQueryParam(final @NonNull HttpUrl url) {
        assertThat(url.queryParameterNames()).isEmpty();
    }

    private static List<String> toExpectedValues(final Object value) {
        if (value instanceof Collection<?> collection) {
            return collection.stream()
                             .map(String::valueOf)
                             .toList();
        }

        return List.of(String.valueOf(value));
    }
}
