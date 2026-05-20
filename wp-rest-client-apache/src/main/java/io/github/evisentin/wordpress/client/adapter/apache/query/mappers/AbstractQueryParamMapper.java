package io.github.evisentin.wordpress.client.adapter.apache.query.mappers;

import io.github.evisentin.wordpress.client.domain.model.enums.WpHasValueEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.net.URIBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Base class for mapping query objects to HTTP query parameters.
 *
 * <p>Provides a set of helper methods to safely add query parameters to an
 * {@link URIBuilder}. All methods are null-safe and only add parameters when values are present and valid.
 *
 * <p>This class is intended for internal use and is extended by concrete
 * query mapper implementations.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AbstractQueryParamMapper {

    protected static void addBoolean(final URIBuilder builder,
                                     final String paramName, final Boolean value) {
        ofNullable(value)
                .map(Object::toString)
                .ifPresent(val -> builder.addParameter(paramName, val));
    }

    protected static void addEnum(final URIBuilder builder,
                                  final String paramName, final WpHasValueEnum value) {
        ofNullable(value)
                .map(WpHasValueEnum::getValue)
                .ifPresent(val -> builder.addParameter(paramName, val));
    }

    protected static void addInteger(final URIBuilder builder,
                                     final String paramName, final Integer value) {
        ofNullable(value)
                .ifPresent(val -> builder.addParameter(paramName, val.toString()));
    }

    protected static void addLocalDate(final URIBuilder builder,
                                       final String paramName, final LocalDate value) {
        ofNullable(value)
                .ifPresent(val -> builder.addParameter(paramName, val.format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    protected static void addLong(final URIBuilder builder,
                                  final String paramName, final Long value) {
        ofNullable(value)
                .ifPresent(val -> builder.addParameter(paramName, val.toString()));
    }

    protected static void addSetOfEnums(final URIBuilder builder,
                                        final String paramName, final Set<? extends WpHasValueEnum> values) {

        final String paramValue = emptyIfNull(values).stream()
                                                     .map(WpHasValueEnum::getValue)
                                                     .sorted()
                                                     .collect(joining(","));
        if (isNotBlank(paramValue))
            builder.addParameter(paramName, paramValue);
    }

    protected static void addSetOfLong(final URIBuilder builder,
                                       final String paramName, final Set<Long> values) {
        final String paramValue = emptyIfNull(values).stream()
                                                     .sorted()
                                                     .map(Object::toString)
                                                     .collect(joining(","));
        if (isNotBlank(paramValue))
            builder.addParameter(paramName, paramValue);
    }

    protected static void addSetOfStrings(final URIBuilder builder,
                                          final String paramName, final Set<String> values) {
        final String paramValue = emptyIfNull(values).stream()
                                                     .map(StringUtils::trimToEmpty)
                                                     .filter(StringUtils::isNotBlank)
                                                     .sorted()
                                                     .distinct()
                                                     .collect(joining(","));
        if (isNotBlank(paramValue))
            builder.addParameter(paramName, paramValue);
    }

    protected static void addString(final URIBuilder builder,
                                    final String paramName, final String value) {
        ofNullable(trimToNull(value))
                .ifPresent(val -> builder.addParameter(paramName, val));
    }

    private static <T> Set<T> emptyIfNull(final Set<T> set) {
        return ofNullable(set).orElseGet(Collections::emptySet);
    }
}
