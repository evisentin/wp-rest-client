package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import com.ev.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.ev.wordpress.client.adapter.okhttp.constants.HttpHeaders.AUTHORIZATION;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest implements WithAssertions {

    private final WpAuthenticationStrategy strategy = new WpBasicAuthenticationStrategy("user", "password");

    @Mock
    private Interceptor.Chain chain;

    @Mock
    private Response response;

    private AuthenticationInterceptor interceptor;

    @Test
    @DisplayName("constructor should fail on null strategy")
    void constructorShouldFailOnNullStrategy() {

        assertThatThrownBy(() -> new AuthenticationInterceptor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @BeforeEach
    void setUp() {
        interceptor = new AuthenticationInterceptor(strategy);
    }

    @Test
    @DisplayName("should add authentication header and proceed with updated request")
    void shouldAddAuthenticationHeaderAndProceedWithUpdatedRequest() throws IOException {

        Request originalRequest = new Request.Builder()
                .url("https://example.com/wp-json/posts")
                .get()
                .build();

        when(chain.request()).thenReturn(originalRequest);
        when(chain.proceed(org.mockito.ArgumentMatchers.any())).thenReturn(response);

        Response actual = interceptor.intercept(chain);

        assertThat(actual).isSameAs(response);

        verify(chain).proceed(argThat(request ->
                "Basic dXNlcjpwYXNzd29yZA==".equals(request.header(AUTHORIZATION)) &&
                request.method().equals("GET") &&
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
