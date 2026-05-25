package io.github.evisentin.wordpress.client.domain.auth;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class WpAuthExceptionTest implements WithAssertions {

    @Test
    void shouldCreateExceptionWithMessage() {

        WpAuthException exception =
                new WpAuthException("authentication failed");

        assertThat(exception)
                .hasMessage("authentication failed")
                .hasNoCause();
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {

        RuntimeException cause =
                new RuntimeException("root cause");

        WpAuthException exception =
                new WpAuthException("authentication failed", cause);

        assertThat(exception)
                .hasMessage("authentication failed")
                .hasCause(cause);
    }

    @Test
    void shouldRejectNullCause() {

        assertThatThrownBy(() ->
                new WpAuthException("authentication failed", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMessageInSingleArgumentConstructor() {

        assertThatThrownBy(() -> new WpAuthException(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldRejectNullMessageInTwoArgumentsConstructor() {

        RuntimeException cause =
                new RuntimeException("root cause");

        assertThatThrownBy(() -> new WpAuthException(null, cause))
                .isInstanceOf(NullPointerException.class);
    }
}
