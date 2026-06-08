package io.github.evisentin.wordpress.test.integration.base;

import io.github.evisentin.wordpress.client.domain.exception.WpBadRequestException;
import io.github.evisentin.wordpress.client.domain.exception.WpForbiddenException;
import io.github.evisentin.wordpress.client.domain.exception.WpNotFoundException;
import io.github.evisentin.wordpress.client.domain.exception.WpUnauthorizedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WpAssertions {

    public static void assertThrowsWpBadRequest(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable,
                                                final @NonNull String expectedCode,
                                                final @NonNull String expectedMessage) {
        assertThrowsWpBadRequest(shouldRaiseThrowable, expectedCode, expectedMessage, emptyMap());
    }

    public static void assertThrowsWpBadRequest(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable,
                                                final @NonNull String expectedCode,
                                                final @NonNull String expectedMessage,
                                                final Map<String, Object> extraEntries) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage(expectedMessage)
                .extracting(ex -> (WpBadRequestException) ex)
                .extracting(WpBadRequestException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo(expectedCode);
                    assertThat(error.getMessage()).isEqualTo(expectedMessage);
                    assertThat(error.getData()).containsEntry("status", 400);
                    if (extraEntries != null) {
                        assertThat(error.getData()).containsAllEntriesOf(extraEntries);
                    }
                });
    }

    public static void assertThrowsWpBadRequest(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThrowsWpBadRequest(shouldRaiseThrowable, "rest_missing_callback_param", "Missing parameter(s): param1");
    }

    public static void assertThrowsWpForbidden(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Sorry, you are not allowed to do that.")
                .extracting(ex -> (WpForbiddenException) ex)
                .extracting(WpForbiddenException::getError)
                .satisfies(error -> {
                    Assertions.assertThat(error.getCode()).isEqualTo("rest_forbidden");
                    Assertions.assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                    Assertions.assertThat(error.getData()).containsExactly(Assertions.entry("status", 403));
                });
    }

    public static void assertThrowsWpForbidden(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable,
                                               final @NonNull String expectedCode,
                                               final @NonNull String expectedMessage) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage(expectedMessage)
                .extracting(ex -> (WpForbiddenException) ex)
                .extracting(WpForbiddenException::getError)
                .satisfies(error -> {
                    Assertions.assertThat(error.getCode()).isEqualTo(expectedCode);
                    Assertions.assertThat(error.getMessage()).isEqualTo(expectedMessage);
                    Assertions.assertThat(error.getData()).containsExactly(Assertions.entry("status", 403));
                });
    }

    public static void assertThrowsWpNotFound(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable,
                                              final @NonNull String expectedCode,
                                              final @NonNull String expectedMessage) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage(expectedMessage)
                .extracting(ex -> (WpNotFoundException) ex)
                .extracting(WpNotFoundException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo(expectedCode);
                    assertThat(error.getMessage()).isEqualTo(expectedMessage);
                    assertThat(error.getData()).containsExactly(entry("status", 404));
                });
    }

    public static void assertThrowsWpNotFound(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThrowsWpNotFound(shouldRaiseThrowable, "rest_term_invalid", "Term does not exist.");
    }

    public static void assertThrowsWpUnauthorized(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("You are not currently logged in.")
                .extracting(ex -> (WpUnauthorizedException) ex)
                .extracting(WpUnauthorizedException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                    assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                    assertThat(error.getData()).containsExactly(entry("status", 401));
                });
    }
}
