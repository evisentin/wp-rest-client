package io.github.evisentin.wordpress.rest.client.adapter.apache.query.mappers;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class ParamMapperTest implements WithAssertions {

    protected URIBuilder builder;

    @SneakyThrows
    void assertThatUrlContainsExactly(final @NonNull URIBuilder uriBuilder,
                                      final @NonNull Map<String, ?> parameters) {

        Map<String, List<String>> paramMap =
                URLEncodedUtils.parse(uriBuilder.build(), StandardCharsets.UTF_8)
                               .stream()
                               .collect(Collectors.groupingBy(
                                       NameValuePair::getName,
                                       Collectors.mapping(NameValuePair::getValue, Collectors.toList())
                               ));

        assertThat(paramMap.keySet())
                .containsExactlyInAnyOrderElementsOf(parameters.keySet());

        parameters.forEach((key, value) -> {
            final List<String> actualValues = paramMap.get(key);
            final List<String> expectedValues = toExpectedValues(value);

            assertThat(actualValues)
                    .withFailMessage(
                            "Expected query param '%s' to contain exactly %s but was %s",
                            key, expectedValues, actualValues
                    )
                    .containsExactlyElementsOf(expectedValues);
        });
    }

    void assertThatUrlContainsNoQueryParam(final URIBuilder uriBuilder) {
        assertThat(uriBuilder.getQueryParams()).isEmpty();
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        builder = new URIBuilder("http://localhost");
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
