package com.ev.wordpress.client.adapter.okhttp.interceptors;

import com.ev.wordpress.client.domain.auth.WpAuthenticationStrategy;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private WpAuthenticationStrategy strategy;

    @Mock
    private Interceptor.Chain chain;

    @InjectMocks
    private AuthenticationInterceptor interceptor;

    @Test
    void shouldAddAuthenticationHeaderAndProceedRequest() throws IOException {
        RequestBody body = RequestBody.create(
                "{\"title\":\"hello\"}",
                MediaType.get("application/json")
        );

        Request originalRequest = new Request.Builder()
                .url("https://example.com/wp-json/wp/v2/posts")
                .post(body)
                .build();

        Response expectedResponse = new Response.Builder()
                .request(originalRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build();

        when(chain.request()).thenReturn(originalRequest);
        when(strategy.getHeaderName()).thenReturn("Authorization");
        when(strategy.getHeaderValue()).thenReturn("Bearer token-123");
        when(chain.proceed(any(Request.class))).thenReturn(expectedResponse);

        Response actualResponse = interceptor.intercept(chain);

        assertThat(actualResponse).isSameAs(expectedResponse);

        InOrder inOrder = inOrder(chain, strategy);
        inOrder.verify(chain).request();
        inOrder.verify(strategy).getHeaderName();
        inOrder.verify(strategy).getHeaderValue();
        inOrder.verify(chain).proceed(argThat(request ->
                "POST".equals(request.method())
                && body.equals(request.body())
                && "Bearer token-123".equals(request.header("Authorization"))
                && "https://example.com/wp-json/wp/v2/posts".equals(request.url().toString())
        ));
        inOrder.verifyNoMoreInteractions();

        verifyNoMoreInteractions(chain, strategy);
    }

    @Test
    void shouldThrowExceptionWhenChainIsNull() {
        assertThatThrownBy(() -> interceptor.intercept(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("chain");
    }

    @Test
    void shouldThrowExceptionWhenStrategyIsNull() {
        assertThatThrownBy(() -> new AuthenticationInterceptor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("strategy");
    }
}
