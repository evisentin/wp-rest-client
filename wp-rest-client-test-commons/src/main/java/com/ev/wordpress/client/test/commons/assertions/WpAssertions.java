package com.ev.wordpress.client.test.commons.assertions;

import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.client.domain.exception.WpForbiddenException;
import com.ev.wordpress.client.domain.exception.WpNotFoundException;
import com.ev.wordpress.client.domain.exception.WpUnauthorizedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WpAssertions {

    public static void assertThrowsWpBadRequest(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Missing parameter(s): param1")
                .extracting(ex -> (WpBadRequestException) ex)
                .extracting(WpBadRequestException::getError)
                .satisfies(error -> {
                    Assertions.assertThat(error.getCode()).isEqualTo("rest_missing_callback_param");
                    Assertions.assertThat(error.getMessage()).isEqualTo("Missing parameter(s): param1");
                    Assertions.assertThat(error.getData()).contains(Assertions.entry("status", 400));
                });
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

    public static void assertThrowsWpNotFound(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("Term does not exist.")
                .extracting(ex -> (WpNotFoundException) ex)
                .extracting(WpNotFoundException::getError)
                .satisfies(error -> {
                    Assertions.assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                    Assertions.assertThat(error.getMessage()).isEqualTo("Term does not exist.");
                    Assertions.assertThat(error.getData()).containsExactly(Assertions.entry("status", 404));
                });
    }

    public static void assertThrowsWpUnauthorized(final @NonNull ThrowableAssert.ThrowingCallable shouldRaiseThrowable) {
        assertThatThrownBy(shouldRaiseThrowable)
                .hasMessage("You are not currently logged in.")
                .extracting(ex -> (WpUnauthorizedException) ex)
                .extracting(WpUnauthorizedException::getError)
                .satisfies(error -> {
                    Assertions.assertThat(error.getCode()).isEqualTo("rest_not_logged_in");
                    Assertions.assertThat(error.getMessage()).isEqualTo("You are not currently logged in.");
                    Assertions.assertThat(error.getData()).containsExactly(Assertions.entry("status", 401));
                });
    }
}
