package com.ev.wordpress.test.local.testcontainers.base;

import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.exception.WpBadRequestException;
import com.ev.wordpress.test.local.testcontainers.BaseWordPressIntegrationTest;
import com.ev.wordpress.test.local.testcontainers.base.factory.WpRestClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
 *   <li>REST clients are initialized via {@link #clientFactory()}</li>
 * </ul>
 *
 * <h2>Extensibility</h2>
 * <p>Concrete subclasses must provide a {@link WpRestClientFactory} implementation
 * by overriding {@link #clientFactory()} to control how REST clients are created.</p>
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
 * @see WpRestClientFactory
 */
@Slf4j
public abstract class BasicAuthWordPressIntegrationTest extends BaseWordPressIntegrationTest {

    protected WpRestClient adminClient;
    protected WpRestClient standardUserClient;

    @BeforeAll
    void installWordpress() {

        log.info("installWordpress: BEGIN");

        if (!wpIsWordPressInstalled()) {
            log.info("installWordpress: initializing...");
            wpInitWordPress(getHttpsBaseUrl());
            wpConfigureDefaultUsers();
            wpActivatePermalinks();
            wpCleanDefaultData();
            log.info("installWordpress: initialized");
        }

        log.info("installWordpress: END");

        adminClient = clientFactory().create(
                getHttpsBaseUrl(),
                WP_ADMIN_USER_NAME,
                adminApplicationPassword
        );

        standardUserClient = clientFactory().create(
                getHttpsBaseUrl(),
                WP_STANDARD_USER_NAME,
                WP_STANDARD_USER_PASSWORD
        );
    }

    protected abstract WpRestClientFactory clientFactory();

    @DisplayName("Category APIs - Integration Tests")
    @Nested
    class CategoryTests {
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingParentId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest creationRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName("my category")
                                                 .withParentId(nonExistingParentId)
                                                 .build();

            assertThatThrownBy(() -> adminClient.createCategory(creationRequest))
                    .hasMessage("Parent term does not exist.")
                    .extracting(ex -> (WpBadRequestException) ex)
                    .extracting(WpBadRequestException::getError)
                    .satisfies(error -> {
                        assertThat(error.getCode()).isEqualTo("rest_term_invalid");
                        assertThat(error.getMessage()).isEqualTo("Parent term does not exist.");
                        assertThat(error.getData()).containsExactly(entry("status", 400));
                    });
        }
    }
}
