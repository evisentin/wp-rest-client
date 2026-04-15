package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private WpAuthenticationStrategy strategy;

    @Mock
    private Interceptor.Chain chain;

    @Mock
    private Response response;

    @InjectMocks
    private AuthenticationInterceptor interceptor;

    @Test
    @DisplayName("constructor should fail on null strategy")
    void constructorShouldFailOnNullStrategy() {

        assertThatThrownBy(() -> new AuthenticationInterceptor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    @DisplayName("should add authentication header and proceed with updated request")
    void shouldAddAuthenticationHeaderAndProceedWithUpdatedRequest() throws IOException {
        Request originalRequest = new Request.Builder()
                .url("https://example.com/wp-json/posts")
                .get()
                .build();

        when(chain.request()).thenReturn(originalRequest);
        when(chain.proceed(argThat(request ->
                "Bearer token-123".equals(request.header("Authorization")) &&
                "GET".equals(request.method()) &&
                request.body() == originalRequest.body()
        ))).thenReturn(response);

        when(strategy.getHeaderName()).thenReturn("Authorization");
        when(strategy.getHeaderValue()).thenReturn("Bearer token-123");

        Response actual = interceptor.intercept(chain);

        assertThat(actual).isSameAs(response);

        verify(strategy).getHeaderName();
        verify(strategy).getHeaderValue();
        verify(chain).request();
        verify(chain).proceed(argThat(request ->
                "Bearer token-123".equals(request.header("Authorization")) &&
                "GET".equals(request.method()) &&
                request.body() == originalRequest.body()
        ));
    }

    @Test
    @DisplayName("should fail on null chain")
    void shouldFailOnNullChain() {

        assertThatThrownBy(() -> interceptor.intercept(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("chain is marked non-null but is null");
    }
}
