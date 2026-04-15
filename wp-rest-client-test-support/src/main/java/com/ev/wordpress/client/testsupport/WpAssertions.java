package com.ev.wordpress.client.testsupport;

import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.client.domain.exception.WpForbiddenException;
import com.ev.wordpress.client.domain.exception.WpNotFoundException;
import com.ev.wordpress.client.domain.exception.WpUnauthorizedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WpAssertions {

    public static void assertThrowsWpBadRequest(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Missing parameter(s): param1")
                .extracting(ex -> (WpBadRequestException) ex)
                .extracting(WpBadRequestException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo("rest_missing_callback_param");
                    assertThat(error.getMessage()).isEqualTo("Missing parameter(s): param1");
                    assertThat(error.getData()).contains(entry("status", 400));
                });
    }

    public static void assertThrowsWpForbidden(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Sorry, you are not allowed to do that.")
                .extracting(ex -> (WpForbiddenException) ex)
                .extracting(WpForbiddenException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo("rest_forbidden");
                    assertThat(error.getMessage()).isEqualTo("Sorry, you are not allowed to do that.");
                    assertThat(error.getData()).containsExactly(entry("status", 403));
                });
    }

    public static void assertThrowsWpNotFound(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Term does not exist.")
                .extracting(ex -> (WpNotFoundException) ex)
                .extracting(WpNotFoundException::getError)
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                    assertThat(error.getMessage()).isEqualTo("Term does not exist.");
                    assertThat(error.getData()).containsExactly(entry("status", 404));
                });
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
