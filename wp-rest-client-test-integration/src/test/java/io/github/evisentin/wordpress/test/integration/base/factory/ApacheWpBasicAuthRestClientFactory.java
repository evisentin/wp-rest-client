package io.github.evisentin.wordpress.test.integration.base.factory;

import io.github.evisentin.wordpress.client.adapter.apache.ApacheWpRestClientBuilder;
import io.github.evisentin.wordpress.client.domain.WpRestClient;
import io.github.evisentin.wordpress.client.domain.configuration.SslConfiguration;
import io.github.evisentin.wordpress.test.integration.BaseWordPressIntegrationTest;
import io.github.evisentin.wordpress.test.integration.base.BasicAuthWordPressIntegrationTest;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Base integration test class for WordPress REST API scenarios using Basic Authentication.
 *
 * <p>This class extends {@link BaseWordPressIntegrationTest} and provides a
 * preconfigured WordPress instance along with ready-to-use REST clients for authenticated testing. It focuses
 * specifically on testing API interactions secured via Basic Authentication.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Ensures WordPress is installed and initialized before tests run</li>
 *   <li>Configures default users and cleans initial data</li>
 *   <li>Initializes REST clients for both admin and standard users</li>
 *   <li>Provides a foundation for API-level integration tests</li>
 * </ul>
 *
 * <h2>Authentication Setup</h2>
 * <p>Two {@link WpRestClient} instances are created:</p>
 * <ul>
 *   <li>{@code adminClient} – authenticated as an administrator using an application password</li>
 *   <li>{@code standardUserClient} – authenticated as a regular user</li>
 * </ul>
 *
 * <h2>Lifecycle</h2>
 * <p>Before all tests, the class ensures that:</p>
 * <ul>
 *   <li>WordPress is installed (if not already)</li>
 *   <li>Default users are created</li>
 *   <li>Permalinks are enabled</li>
 *   <li>Default content is removed</li>
 *   <li>REST clients are initialized via {@link BasicAuthWordPressIntegrationTest#clientFactory()}</li>
 * </ul>
 *
 * <h2>Extensibility</h2>
 * <p>Concrete subclasses must provide a {@link WpBasicAuthRestClientFactory} implementation
 * by overriding {@link BasicAuthWordPressIntegrationTest#clientFactory()} to control how REST clients are created.</p>
 *
 * <h2>Typical Usage</h2>
 * <pre>{@code
 * class MyApiTest extends BasicAuthWordPressIntegrationTest {
 *
 *     @Override
 *     protected WpRestClientFactory clientFactory() {
 *         return new MyClientFactory();
 *     }
 *
 *     @Test
 *     void shouldCreateCategory() {
 *         // use adminClient or standardUserClient
 *     }
 * }
 * }</pre>
 *
 * @see BaseWordPressIntegrationTest
 * @see WpRestClient
 * @see WpBasicAuthRestClientFactory
 */
public final class ApacheWpBasicAuthRestClientFactory implements WpBasicAuthRestClientFactory {

    private final SslConfiguration sslConfiguration;

    public ApacheWpBasicAuthRestClientFactory(final @NonNull SslConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    @Override
    public WpRestClient create(final @NonNull String baseUrl,
                               final @NonNull String username,
                               final @NonNull String password) {
        return ApacheWpRestClientBuilder.basicAuthentication(baseUrl, username, password)
                                        .withSslConfiguration(sslConfiguration)
                                        .build();
    }
}
