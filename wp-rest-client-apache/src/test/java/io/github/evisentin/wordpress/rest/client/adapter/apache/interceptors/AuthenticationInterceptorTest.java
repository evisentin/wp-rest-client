package io.github.evisentin.wordpress.rest.client.adapter.apache.interceptors;

import io.github.evisentin.wordpress.rest.client.domain.auth.WpAuthenticationStrategy;
import io.github.evisentin.wordpress.rest.client.domain.auth.WpBasicAuthenticationStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest implements WithAssertions {

    private final WpAuthenticationStrategy strategy = new WpBasicAuthenticationStrategy("user", "password");

    @Mock
    private CloseableHttpClient authHttpClient;

    @Mock
    private HttpRequest request;

    @Mock
    private EntityDetails entityDetails;

    @Mock
    private HttpContext context;

    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new AuthenticationInterceptor(strategy, authHttpClient, "");
    }

    @Test
    @DisplayName("should fail on null strategy")
    void shouldAFailOnNullStrategy() {

        assertThatThrownBy(() -> new AuthenticationInterceptor(null, null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");

        assertThatThrownBy(() -> new AuthenticationInterceptor(new WpBasicAuthenticationStrategy("user", "password"), null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("authHttpClient is marked non-null but is null");

        assertThatThrownBy(() -> new AuthenticationInterceptor(new WpBasicAuthenticationStrategy("user", "password"), HttpClients.createDefault(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("apiUrl is marked non-null but is null");
    }

    @Test
    @DisplayName("should add authentication header to request")
    void shouldAddAuthenticationHeaderToRequest() {

        assertThatCode(() -> interceptor.process(request, entityDetails, context))
                .doesNotThrowAnyException();

        verify(request).addHeader("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
    }
}
