package io.github.evisentin.wordpress.client.domain.auth.header;

import io.github.evisentin.wordpress.client.domain.auth.WpBasicAuthenticationStrategy;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class BasicAuthenticationHeaderProviderTest implements WithAssertions {

    private final BasicAuthenticationHeaderProvider provider = new BasicAuthenticationHeaderProvider();

    @Test
    void shouldCreateBasicAuthorizationHeader() {
        WpBasicAuthenticationStrategy strategy = new WpBasicAuthenticationStrategy("user", "password");

        String header = provider.createAuthorizationHeader(strategy);

        assertThat(header)
                .isEqualTo("Basic dXNlcjpwYXNzd29yZA==");
    }
}
