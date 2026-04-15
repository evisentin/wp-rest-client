package com.ev.wordpress.client.adapter.apache.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private WpAuthenticationStrategy strategy;

    @Mock
    private HttpRequest request;

    @Mock
    private EntityDetails entityDetails;

    @Mock
    private HttpContext context;

    @InjectMocks
    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        lenient().when(strategy.getHeaderName()).thenReturn("Authorization");
        lenient().when(strategy.getHeaderValue()).thenReturn("Bearer test-token");
    }

    @Test
    @DisplayName("should fail on null strategy")
    void shouldAFailOnNullStrategy() {

        assertThatThrownBy(() -> new AuthenticationInterceptor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    @DisplayName("should add authentication header to request")
    void shouldAddAuthenticationHeaderToRequest() {
        assertThatCode(() -> interceptor.process(request, entityDetails, context))
                .doesNotThrowAnyException();

        verify(strategy).getHeaderName();
        verify(strategy).getHeaderValue();
        verify(request).addHeader("Authorization", "Bearer test-token");
    }
}
