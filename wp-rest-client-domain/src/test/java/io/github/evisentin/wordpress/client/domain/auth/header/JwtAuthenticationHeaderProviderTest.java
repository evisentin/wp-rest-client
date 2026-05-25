package io.github.evisentin.wordpress.client.domain.auth.header;

import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtTokenClient;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationHeaderProviderTest implements WithAssertions {

    @Mock
    private JwtTokenClient tokenClient;

    @Mock
    private JwtResponse response;

    @InjectMocks
    private JwtAuthenticationHeaderProvider provider;

    @Test
    void constructorFailsOnNull() {
        assertThatThrownBy(() -> new JwtAuthenticationHeaderProvider(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("tokenClient is marked non-null but is null");
    }

    @Test
    void createAuthorizationHeader_shouldFailOnNull() {
        assertThatThrownBy(() -> provider.createAuthorizationHeader(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldCreateBearerAuthorizationHeader() {

        when(response.getJwtToken()).thenReturn("jwt-token");
        when(response.getExpiresIn()).thenReturn(3600L);
        when(response.getIat()).thenReturn(System.currentTimeMillis() / 1000);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenReturn(response);

        String header = provider.createAuthorizationHeader(strategy);

        assertThat(header)
                .isEqualTo("Bearer jwt-token");

        verify(tokenClient).fetchToken(strategy);
    }

    @Test
    void shouldReuseCachedToken() {

        when(response.getJwtToken()).thenReturn("jwt-token");
        when(response.getExpiresIn()).thenReturn(3600L);
        when(response.getIat()).thenReturn(System.currentTimeMillis() / 1000);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenReturn(response);

        provider.createAuthorizationHeader(strategy);
        provider.createAuthorizationHeader(strategy);

        verify(tokenClient, times(1)).fetchToken(strategy);
    }
}
