package io.github.evisentin.wordpress.client.domain.auth.header;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.evisentin.wordpress.client.domain.auth.WpJwtAuthenticationStrategy;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtResponse;
import io.github.evisentin.wordpress.client.domain.auth.jwt.JwtTokenClient;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

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
    void createAuthorizationHeaderFetchesNewTokenWhenCachedTokenHasExpired() {

        final String expiredToken = createExpiredToken();
        final String validToken = createToken();

        // Simulate the token endpoint returning an expired token first,
        // then a fresh valid token when a refresh is required.
        when(response.getToken())
                .thenReturn(expiredToken, validToken);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenReturn(response);

        // First invocation populates the provider cache with an expired token.
        // The provider does not validate freshness until the next access.
        provider.createAuthorizationHeader(strategy);

        // Second invocation detects the cached token is expired,
        // fetches a new one, caches it, and returns it.
        String header = provider.createAuthorizationHeader(strategy);

        assertThat(header)
                .isEqualTo("Bearer " + validToken);

        // One fetch for the initial token and one fetch for the refresh.
        verify(tokenClient, times(2))
                .fetchToken(strategy);
    }

    @Test
    void createAuthorizationHeader_shouldFailOnNull() {
        assertThatThrownBy(() -> provider.createAuthorizationHeader(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("strategy is marked non-null but is null");
    }

    @Test
    void shouldCreateBearerAuthorizationHeader() {

        final String token = createToken();

        when(response.getToken()).thenReturn(token);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenReturn(response);

        String header = provider.createAuthorizationHeader(strategy);

        assertThat(header)
                .isEqualTo("Bearer " + token);

        verify(tokenClient).fetchToken(strategy);
    }

    @Test
    void shouldCreateBearerAuthorizationHeaderOnExpired() {

        final String token = createExpiredToken();

        when(response.getToken()).thenReturn(token);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenReturn(response);

        String header = provider.createAuthorizationHeader(strategy);

        assertThat(header)
                .isEqualTo("Bearer " + token);

        verify(tokenClient).fetchToken(strategy);
    }

    @Test
    void shouldFetchTokenOnlyOnceWhenCalledConcurrently() throws Exception {

        final String token = createToken();

        when(response.getToken()).thenReturn(token);

        WpJwtAuthenticationStrategy strategy =
                new WpJwtAuthenticationStrategy(
                        "user",
                        "password",
                        "https://example.com/jwt");

        when(tokenClient.fetchToken(strategy)).thenAnswer(invocation -> {
            // Simulate a slow remote call so that multiple threads
            // reach createAuthorizationHeader() concurrently while
            // the first thread is still inside the synchronized block.
            Thread.sleep(100);

            return response;
        });

        int threads = 20;

        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {

            // Used to release all worker threads at the same time,
            // maximizing contention on the synchronized section.
            CountDownLatch start = new CountDownLatch(1);

            List<Callable<String>> tasks =
                    IntStream.range(0, threads)
                             .mapToObj(i -> (Callable<String>) () -> {
                                 // Wait until all tasks are ready before
                                 // invoking createAuthorizationHeader().
                                 start.await();

                                 return provider.createAuthorizationHeader(strategy);
                             })
                             .toList();

            // Submit all concurrent tasks to the thread pool.
            List<Future<String>> futures = tasks.stream()
                                                .map(executor::submit)
                                                .toList();

            // Release all waiting threads simultaneously.
            start.countDown();

            // Every thread should receive the same cached bearer token.
            for (Future<String> future : futures) {
                assertThat(future.get()).isEqualTo("Bearer " + token);
            }

            executor.shutdown();
        }

        // Verify that only one thread actually fetched the token.
        // All other threads should pass the second hasValidToken()
        // check inside the synchronized block and reuse the cached token.
        verify(tokenClient, times(1)).fetchToken(strategy);
    }

    @Test
    void shouldReuseCachedToken() {

        final String token = createToken();

        when(response.getToken()).thenReturn(token);

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

    private static String createExpiredToken() {
        final Instant now = Instant.now();

        return JWT.create()
                  .withSubject("1")
                  .withClaim("name", "admin")
                  .withIssuedAt(Date.from(now))
                  .withExpiresAt(Date.from(now.minus(1, ChronoUnit.HOURS)))
                  .sign(Algorithm.HMAC256("test-secret"));
    }

    private static String createToken() {
        final Instant now = Instant.now();
        return JWT.create()
                  .withSubject("1")
                  .withClaim("name", "admin")
                  .withIssuedAt(Date.from(now))
                  .withExpiresAt(Date.from(now.plus(1, ChronoUnit.HOURS)))
                  .sign(Algorithm.HMAC256("test-secret"));
    }
}
