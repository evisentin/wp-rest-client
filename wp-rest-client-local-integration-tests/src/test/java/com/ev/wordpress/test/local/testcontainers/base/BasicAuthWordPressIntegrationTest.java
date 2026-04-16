package com.ev.wordpress.test.local.testcontainers.base;

import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.assertions.WordPressAssertions;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.test.local.testcontainers.BaseWordPressIntegrationTest;
import com.ev.wordpress.test.local.testcontainers.base.factory.WpRestClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.ev.wordpress.test.local.assertions.WpAssertions.assertThrowsWpBadRequest;
import static java.util.Collections.emptyMap;

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

        private final static String CATEGORY_TEST_NAME = "Category #1";
        private final static String CATEGORY_TEST_DESCRIPTION = "My first category";
        private final static String CATEGORY_TEST_SLUG = "category-1";

        @DisplayName("'CREATE' fails on parent not found")
        @Test
        void create__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingParentId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest creationRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_TEST_NAME)
                                                 .withParentId(nonExistingParentId)
                                                 .build();

            assertThrowsWpBadRequest(() -> adminClient.createCategory(creationRequest),
                    "rest_term_invalid",
                    "Parent term does not exist.",
                    emptyMap());
        }

        @DisplayName("'CREATE' works")
        @Test
        void create__works() {

            // GIVEN
            wpCleanDefaultData();

            Long existingParentCategory = wpCreateCategory("Parent", "Parent Category", "parent");

            // WHEN
            final WpCategory category = adminClient.createCategory(WpCategoryCreateUpdateRequest.builder()
                                                                                                .withName(CATEGORY_TEST_NAME)
                                                                                                .withDescription(CATEGORY_TEST_DESCRIPTION)
                                                                                                .withSlug(CATEGORY_TEST_SLUG)
                                                                                                .withParentId(existingParentCategory)
                                                                                                .build());
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasNonZeroId()
                               .hasName(CATEGORY_TEST_NAME)
                               .hasDescription(CATEGORY_TEST_DESCRIPTION)
                               .hasSlug(CATEGORY_TEST_SLUG)
                               .hasParentId(existingParentCategory)
                               .hasCount(0)
                               .hasTaxonomy(WpTaxonomy.CATEGORY)
                               .hasLink("");
        }
    }
}
