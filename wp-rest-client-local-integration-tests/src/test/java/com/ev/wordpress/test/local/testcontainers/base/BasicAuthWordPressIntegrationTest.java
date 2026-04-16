package com.ev.wordpress.test.local.testcontainers.base;

import com.ev.wordpress.client.domain.api.WpRestClient;
import com.ev.wordpress.client.domain.assertions.WordPressAssertions;
import com.ev.wordpress.client.domain.dto.WpCategory;
import com.ev.wordpress.client.domain.dto.enums.WpContext;
import com.ev.wordpress.client.domain.dto.enums.WpTaxonomy;
import com.ev.wordpress.client.domain.dto.requests.WpCategoryCreateUpdateRequest;
import com.ev.wordpress.client.domain.dto.responses.WpCategoryDeletionResponse;
import com.ev.wordpress.test.local.testcontainers.BaseWordPressIntegrationTest;
import com.ev.wordpress.test.local.testcontainers.base.factory.WpRestClientFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.ev.wordpress.client.domain.dto.enums.WpTaxonomy.CATEGORY;
import static com.ev.wordpress.test.local.assertions.WpAssertions.assertThrowsWpBadRequest;
import static com.ev.wordpress.test.local.assertions.WpAssertions.assertThrowsWpNotFound;
import static com.ev.wordpress.test.utils.SlugUtils.toWordPressSlug;

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
                    "Parent term does not exist.");
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
                               .hasLink(linkForCategory("parent", CATEGORY_TEST_SLUG));
        }

        @DisplayName("'DELETE' fails on HTTP NOT FOUND")
        @Test
        void delete__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.deleteCategory(1000L), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'DELETE' works")
        @Test
        void delete__works() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN
            final WpCategoryDeletionResponse deletionResponse = adminClient.deleteCategory(categoryId);

            // THEN
            WordPressAssertions.assertThat(deletionResponse)
                               .isNotNull()
                               .isDeleted()
                               .hasPreviousSatisfying(summary ->
                                       summary.isNotNull()
                                              .hasId(categoryId)
                                              .hasCount(0)
                                              .hasDescription(CATEGORY_TEST_DESCRIPTION)
                                              .hasName(CATEGORY_TEST_NAME)
                                              .hasSlug(CATEGORY_TEST_SLUG)
                                              .hasLink(linkForCategory(CATEGORY_TEST_SLUG))
                                              .hasTaxonomy(WpTaxonomy.CATEGORY));
        }

        @DisplayName("'GET' fails on HTTP NOT FOUND")
        @Test
        void get__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN/THEN
            assertThrowsWpNotFound(() -> adminClient.getCategory(categoryId + 1000, null), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'GET' works with context")
        @Test
        void get__works_with_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN
            final WpCategory category = adminClient.getCategory(categoryId, WpContext.EDIT);

            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_TEST_DESCRIPTION)
                               .hasName(CATEGORY_TEST_NAME)
                               .hasSlug(CATEGORY_TEST_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        @DisplayName("'GET' works with no context")
        @Test
        void get__works_with_no_context() {

            // GIVEN
            wpCleanDefaultData();
            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN
            final WpCategory category = adminClient.getCategory(categoryId, null);

            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_TEST_DESCRIPTION)
                               .hasName(CATEGORY_TEST_NAME)
                               .hasSlug(CATEGORY_TEST_SLUG)
                               .hasTaxonomy(CATEGORY);
        }

        // list__works_with_just_paging
        // list__works_with_paging_and_query

        @DisplayName("'UPDATE' fails on HTTP NOT FOUND")
        @Test
        void update__fails_on_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingCategoryId = 1000L;

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest updateRequest = WpCategoryCreateUpdateRequest.builder().build();
            assertThrowsWpNotFound(() -> adminClient.updateCategory(nonExistingCategoryId, updateRequest), "rest_term_invalid", "Term does not exist.");
        }

        @DisplayName("'UPDATE' fails on parent not found")
        @Test
        void update__fails_on_parent_not_found() {

            // GIVEN
            wpCleanDefaultData();

            Long nonExistingCategoryId = 1000L;
            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN/THEN

            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName("my category")
                                                 .withParentId(nonExistingCategoryId)
                                                 .build();

            assertThrowsWpBadRequest(() -> adminClient.updateCategory(categoryId, updateRequest),
                    "rest_term_invalid", "Parent term does not exist.");
        }

        @DisplayName("'UPDATE' works")
        @Test
        void update__works() {

            // GIVEN
            wpCleanDefaultData();

            final Long categoryId = wpCreateCategory(CATEGORY_TEST_NAME, CATEGORY_TEST_DESCRIPTION, CATEGORY_TEST_SLUG);

            // WHEN
            final WpCategoryCreateUpdateRequest updateRequest =
                    WpCategoryCreateUpdateRequest.builder()
                                                 .withName(CATEGORY_TEST_NAME + " UPDATED")
                                                 .withDescription(CATEGORY_TEST_DESCRIPTION + " UPDATED")
                                                 .withSlug(CATEGORY_TEST_SLUG + " UPDATED")
                                                 .build();

            final WpCategory category = adminClient.updateCategory(categoryId, updateRequest);

            // THEN
            WordPressAssertions.assertThat(category)
                               .isNotNull()
                               .hasId(categoryId)
                               .hasCount(0)
                               .hasDescription(CATEGORY_TEST_DESCRIPTION + " UPDATED")
                               .hasName(CATEGORY_TEST_NAME + " UPDATED")
                               .hasSlug(toWordPressSlug(CATEGORY_TEST_SLUG + " UPDATED"))
                               .hasTaxonomy(CATEGORY);
        }

        private String linkForCategory(final @NonNull String slug) {
            return String.format("%s/category/%s/", getHttpsBaseUrl(), slug);
        }

        private String linkForCategory(final @NonNull String parentSlug, final @NonNull String slug) {
            return String.format("%s/category/%s/%s/", getHttpsBaseUrl(), parentSlug, slug);
        }
    }
}
